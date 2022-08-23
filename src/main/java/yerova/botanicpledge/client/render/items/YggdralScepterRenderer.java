package yerova.botanicpledge.client.render.items;

import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import yerova.botanicpledge.common.items.YggdralScepter;

public class YggdralScepterRenderer extends GeoItemRenderer<YggdralScepter> {
    public YggdralScepterRenderer() {
        super(new YggdralScepterModel());
    }
}
