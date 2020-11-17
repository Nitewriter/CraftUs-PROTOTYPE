package com.abidux.craftus.game;

public class CoolDown {
    private final Integer duration;
    private Integer remainingDuration;

    public CoolDown(Integer duration) {
        this.duration = duration;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getRemainingDuration() {
        return remainingDuration;
    }

    public boolean isCleared() {
        return remainingDuration == 0;
    }

    public void clear() {
        remainingDuration = 0;
    }

    public void reset() {
        remainingDuration = duration;
    }

    public void update() {
        if (isCleared()) {
            return;
        }

        remainingDuration -= 1;
    }
}
