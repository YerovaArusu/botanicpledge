package yerova.botanicpledge.mixin_api;

import yerova.botanicpledge.common.entitites.yggdrasilguardian.YggdrasilGuardian;

public interface IMagicLandmineEntity {
    YggdrasilGuardian botanicpledge$getGuardian();
    void botanicpledge$setGuardian(YggdrasilGuardian customSummoner);
}
