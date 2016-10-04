package com.happylrd.aurora;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 图片加载类（会维护一个Cache），整个项目只有一个
 * Created by lenovo on 2016/9/1.
 */
public class ImageLoader {
    private static ImageLoader mInstance;

    //图片缓存的核心对象
    private LruCache<String,Bitmap> mLruCache;
    //线程池
    private ExecutorService mThreadPool;
    //默认线程数目
    private static final int DEFULT_THREAD_COUNT = 1;
    //队列的调度方式
    private Type mType = Type.LIFO;
    public enum Type{
        FIFO,LIFO
    }
    //任务队列
    private LinkedList<Runnable> mTaskQueue;
    //后台轮询线程
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    //UI线程中的Handler
    private Handler mUIHandler;

    //信号量，用于同步,避免mPoolThreadHandler在初始化之前被调用
    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);

    //
    private Semaphore mSemaphoreThreadPool;

    private ImageLoader(int threadCount,Type type){
        init(threadCount,type);
    }

    private void init(int threadCount, Type type) {
        //后台轮询线程
        mPoolThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        //去线程池取出一个任务进行执行
                        mThreadPool.execute(getTask());
                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //释放一个信号量
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }
        };
        mPoolThread.start();

        //获取我们应用的最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 10;
        mLruCache = new LruCache<String,Bitmap>(cacheMemory){
            //获取每个Bitmap所占的内存
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };

        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        //创建任务队列
        mTaskQueue = new LinkedList<Runnable>();
        mType = type;
        mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    public static ImageLoader getmInstance() {
        //考虑线程和效率
        if(mInstance == null){
            synchronized (ImageLoader.class){
                if(mInstance == null)
                    mInstance = new ImageLoader(DEFULT_THREAD_COUNT, Type.LIFO);
            }
        }
        return mInstance;
    }
    public static ImageLoader getmInstance(int threadCount, Type type) {
        //考虑线程和效率
        if(mInstance == null){
            synchronized (ImageLoader.class){
                if(mInstance == null)
                    mInstance = new ImageLoader(threadCount,type);
            }
        }
        return mInstance;
    }


    //根据path获得图片
    public void loadImage(final String path, final ImageView imageView){

        imageView.setTag(path);
        if(mUIHandler == null){
            mUIHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    //获取得到图片，为imageview回调设置图片
                   ImagBeanHolder obj = (ImagBeanHolder) msg.obj;
                    Bitmap bm = obj.bitmap;
                    String path = obj.path;
                    ImageView image = obj.imageView;
                    if(image.getTag().toString().equals(path)){
                        image.setImageBitmap(bm);
                    }
                }
            };
        }
            Bitmap bm = getBitmapFromLruCache(path);
            if(bm != null){
                refreshBitmap(path, imageView, bm);
            }
            else {
                addTasks(new Runnable(){
                    @Override
                    public void run() {
                        //加载图片
                        //图片的压缩
                        //1、获得图片需要显示的大小
                       ImageSize imageSize =  getImageViewSize(imageView);
                        //2.压缩图片
                        Bitmap bm  = decodeSampledBitmapFromPath(path,imageSize.width,imageSize.height);
                        //3.把图片加入到缓存
                        addBitmapToLruCache(path,bm);
                        refreshBitmap(path, imageView, bm);
                        mSemaphoreThreadPool.release();
                    }
                });
            }
    }
    private void refreshBitmap(final String path,ImageView imageView,Bitmap bm){
        Message message = Message.obtain();
        ImagBeanHolder imagBeanHolder  = new ImagBeanHolder();
        imagBeanHolder.bitmap = bm;
        imagBeanHolder.path = path;
        imagBeanHolder.imageView = imageView;
        message.obj = imagBeanHolder;
        mUIHandler.sendMessage(message);
    }

    //将图片加入缓存（LruCache）
    private void addBitmapToLruCache(String path, Bitmap bm) {
        if(getBitmapFromLruCache(path) == null){
            if (bm!= null)
                mLruCache.put(path,bm);
        }
    }

    //根据图片需要显示的宽和高对图片进行压缩
    private Bitmap decodeSampledBitmapFromPath(String path,int width, int height) {

        //获得图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        options.inSampleSize = caculateInSampleSize(options,width,height);
        //利用获得的inSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        return bitmap;
    }

    //根据图片实际的宽和高以及需求的宽和高计算SampleSize
    private int caculateInSampleSize(BitmapFactory.Options options, int reqwidth, int reqheight) {
        int width = options.outWidth;
        int height  = options.outHeight;
        int inSampleSize = 1;
        if(width >reqwidth || height>reqheight){
            int widthRadio = Math.round(width * 1.0f/reqwidth);
            int heightRadio = Math.round(height * 1.0f/reqheight);
            inSampleSize = Math.max(widthRadio,heightRadio);
        }
        return inSampleSize;
    }

    //根据imageView获得压缩的宽和高
    private ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();
        DisplayMetrics lp = imageView.getContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams IP = imageView.getLayoutParams();
        int width = imageView.getWidth();//获取imageView的实际宽度
        if(width<=0){
            width = IP.width;//获取imageView在layout中声明的宽度
        }
        if(width <= 0){//wrapContent,matchParent = -1,-2;之类的仍然为负值
            width = getImageViewFiledValue(imageView, "mMaxWidth");
           // width = imageView.getMaxWidth();//检查最大值
        }
        if(width <= 0){
            width = lp.widthPixels;
        }

        int height = imageView.getHeight();//获取imageView的实际宽度
        if(height<=0){
            height = IP.height;//获取imageView在layout中声明的宽度
        }
        if(height <= 0){//wrapContent,matchParent = -1,-2;之类的仍然为负值
           // height = imageView.getMaxHeight();//检查最大值
          height = getImageViewFiledValue(imageView,"mMaxHeight");
        }
        if(height <= 0){
            height = lp.heightPixels;
        }
        imageSize.height = height;
        imageSize.width = width;
        return imageSize;
    }

    private  synchronized void addTasks(Runnable runnable) {
        mTaskQueue.add(runnable);
       // if(mPoolThreadHandler == null)wait();
        try {
            if(mPoolThreadHandler == null)
                mSemaphorePoolThreadHandler.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPoolThreadHandler.sendEmptyMessage(110);

    }

    //根据path在LruCache中获得Bitmap
    private Bitmap getBitmapFromLruCache(String path) {
        return mLruCache.get(path);
    }
    private class ImagBeanHolder{
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }

    private class ImageSize{
        int width;
        int height;
    }

    //从任务队列取出一个方法
    private Runnable getTask(){
        if(mType == Type.FIFO)
            return mTaskQueue.removeFirst();
        else if(mType == Type.LIFO)
            return mTaskQueue.removeLast();
        else
            return null;
    }

    //通过反射获取某个组件的某个属性值
    private static int getImageViewFiledValue(Object object,String filedName){
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(filedName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if(fieldValue>0&&fieldValue<Integer.MAX_VALUE)
                value = fieldValue;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }

}
