package com.swam.commons.utility;

import org.oristool.eulero.modeling.stochastictime.UniformTime;

import com.qesm.ProductTemplate;

public class DefaultProducts {
    public static ProductTemplate v0 = new ProductTemplate("v0", 1, new UniformTime(0, 2));
    public static ProductTemplate v1 = new ProductTemplate("v1", 1, new UniformTime(2, 4));
    public static ProductTemplate v2 = new ProductTemplate("v2", 1, new UniformTime(4, 6));
    public static ProductTemplate v3 = new ProductTemplate("v3", 1, new UniformTime(6, 8));
    public static ProductTemplate v4 = new ProductTemplate("v4", 1, new UniformTime(8, 10));

    public static ProductTemplate v5 = new ProductTemplate("v5");
    public static ProductTemplate v6 = new ProductTemplate("v6");
    public static ProductTemplate v7 = new ProductTemplate("v7");
}
