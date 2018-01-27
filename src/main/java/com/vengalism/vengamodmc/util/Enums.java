package com.vengalism.vengamodmc.util;

import com.vengalism.vengamodmc.Config;

/**
 * Created by vengada at 4/11/2017
 */
public class Enums {



    public enum MACHINETIER{ONE("Tier 1", Config.tierOneMultiplier), TWO("Tier 2", Config.tierTwoMultiplier), THREE("Tier 3", Config.tierThreeMultiplier), FOUR("Creative", Config.tierThreeMultiplier);

        private String name = "Tier 1";
        private int multiplier = 1;

        MACHINETIER(String name, int multiplier){
            this.name = name;
            this.multiplier = multiplier;
        }

        public int getMultiplier(){
            return this.multiplier;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String toString() {
            return "name: " + this.name + " Multiplier: " + this.multiplier;
        }
    }
}
