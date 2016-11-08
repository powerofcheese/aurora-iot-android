package com.happylrd.aurora.util;

import android.content.Context;

import com.happylrd.aurora.R;

import java.util.HashMap;
import java.util.Map;

public class ZhToEnMapUtil {

    public Map<String, String> enMotionMap;

    public void initMap(Context context) {
        enMotionMap = new HashMap<>();

        // pattern start from here
        enMotionMap.put(
                context.getString(R.string.pattern_single_color),
                context.getString(R.string.pattern_single_color_en)
        );
        enMotionMap.put(
                context.getString(R.string.pattern_gradation),
                context.getString(R.string.pattern_gradation_en)
        );
        enMotionMap.put(
                context.getString(R.string.pattern_thin_stripe),
                context.getString(R.string.pattern_thin_stripe_en)
        );
        enMotionMap.put(
                context.getString(R.string.pattern_thick_stripe),
                context.getString(R.string.pattern_thick_stripe_en)
        );
        enMotionMap.put(
                context.getString(R.string.pattern_half_stripe),
                context.getString(R.string.pattern_half_stripe_en)
        );
        enMotionMap.put(
                context.getString(R.string.pattern_skip_stripe),
                context.getString(R.string.pattern_skip_stripe_en)
        );

        enMotionMap.put(
                context.getString(R.string.pattern_gradation_skipping),
                context.getString(R.string.pattern_gradation_skipping_en)
        );

        enMotionMap.put(
                context.getString(R.string.pattern_random),
                context.getString(R.string.pattern_random_en)
        );

        enMotionMap.put(
                context.getString(R.string.pattern_random_skipping),
                context.getString(R.string.pattern_random_skipping_en)
        );

        enMotionMap.put(
                context.getString(R.string.pattern_similar_color),
                context.getString(R.string.pattern_similar_color_en)
        );

        enMotionMap.put(
                context.getString(R.string.pattern_custom),
                context.getString(R.string.pattern_custom_en)
        );

        // animation start from here
        enMotionMap.put(
                context.getString(R.string.anim_ramp),
                context.getString(R.string.anim_ramp_en)
        );

        enMotionMap.put(
                context.getString(R.string.anim_wave),
                context.getString(R.string.anim_wave_en)
        );

        enMotionMap.put(
                context.getString(R.string.anim_flash),
                context.getString(R.string.anim_flash_en)
        );

        enMotionMap.put(
                context.getString(R.string.anim_strobe),
                context.getString(R.string.anim_strobe_en)
        );

        enMotionMap.put(
                context.getString(R.string.anim_strobe_in_out),
                context.getString(R.string.anim_strobe_in_out_en)
        );

        enMotionMap.put(
                context.getString(R.string.anim_slow_hue),
                context.getString(R.string.anim_slow_hue_en)
        );

        enMotionMap.put(
                context.getString(R.string.anim_hue_cycle),
                context.getString(R.string.anim_hue_cycle_en)
        );

        enMotionMap.put(
                context.getString(R.string.anim_hue_strobe),
                context.getString(R.string.anim_hue_strobe_en)
        );

        enMotionMap.put(
                context.getString(R.string.anim_nothing),
                context.getString(R.string.anim_nothing_en)
        );

        // rotation start from here
        enMotionMap.put(
                context.getString(R.string.rotation_float_left),
                context.getString(R.string.rotation_float_left_en)
        );

        enMotionMap.put(
                context.getString(R.string.rotation_float_right),
                context.getString(R.string.rotation_float_right_en)
        );

        enMotionMap.put(
                context.getString(R.string.rotation_rotate_left),
                context.getString(R.string.rotation_rotate_left_en)
        );

        enMotionMap.put(
                context.getString(R.string.rotation_rotate_right),
                context.getString(R.string.rotation_rotate_right_en)
        );

        enMotionMap.put(
                context.getString(R.string.rotation_spin_left),
                context.getString(R.string.rotation_spin_left_en)
        );

        enMotionMap.put(
                context.getString(R.string.rotation_spin_right),
                context.getString(R.string.rotation_spin_right_en)
        );

        enMotionMap.put(
                context.getString(R.string.rotation_swing),
                context.getString(R.string.rotation_swing_en)
        );

        enMotionMap.put(
                context.getString(R.string.rotation_switch),
                context.getString(R.string.rotation_switch_en)
        );

        enMotionMap.put(
                context.getString(R.string.rotation_snake),
                context.getString(R.string.rotation_snake_en)
        );

        enMotionMap.put(
                context.getString(R.string.rotation_random_snake),
                context.getString(R.string.rotation_random_snake_en)
        );

        enMotionMap.put(
                context.getString(R.string.rotation_move_to_rotation),
                context.getString(R.string.rotation_move_to_rotation_en)
        );

        enMotionMap.put(
                context.getString(R.string.rotation_nothing),
                context.getString(R.string.rotation_nothing_en)
        );

        // action start from here
        enMotionMap.put(
                context.getString(R.string.action_fade_in_out),
                context.getString(R.string.action_fade_in_out_en)
        );

        enMotionMap.put(
                context.getString(R.string.action_quick_in_out),
                context.getString(R.string.action_quick_in_out_en)
        );

        enMotionMap.put(
                context.getString(R.string.action_power_slow),
                context.getString(R.string.action_power_slow_en)
        );

        enMotionMap.put(
                context.getString(R.string.action_power_quick),
                context.getString(R.string.action_power_quick_en)
        );

        enMotionMap.put(
                context.getString(R.string.action_nothing),
                context.getString(R.string.action_nothing_en)
        );
    }

    public String getEnValueByZhKey(String zhStr) {
        return enMotionMap.get(zhStr);
    }
}
