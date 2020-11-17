package com.abidux.craftus.enums;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import lombok.Getter;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public enum PlayerSkinOld {
    /**
     * MineSkin (233730070)
     *
     * @link https://minesk.in/233730070
     */
    @SuppressWarnings("SpellCheckingInspection")
    RED("ewogICJ0aW1lc3RhbXAiIDogMTYwMzAyMDQ5OTU1MSwKICAicHJvZmlsZUlkIiA6ICIxNzhmMTJkYWMzNTQ0ZjRhYjExNzkyZDc1MDkzY2JmYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJzaWxlbnRkZXRydWN0aW9uIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2NhNGRhNjdhYTA1MTA4MjY0NjM5NWI4ZGZmZmJlOGFkZTA5NDY4ZWM5NWUxNzkzNWMyYWY1OGMwOWM2ZWE4NmIiCiAgICB9CiAgfQp9",
        "BN+4RJ1IRvDiUtz1EXG1MFZeJT8gbrqIj1VUNvTAm/fltKL3iJlDLBsxiolXRNna9l0xtyy07M8v5laLEtm3h4+Tn9oEApvutt8Zk" +
        "/q1VenxWtJ4x6zEereLKS3sk87+owWIi7dPqONQLugfENDZEUekTOe+D+2lnP6+bIy0h6txCbLg+ngBRlF9lTL8MyGYyfwPrfy1Q" +
        "/ebXkO30fqWdhR3WjRZrSjxHjDBZdIdzDdEL7p8UIgw5k" +
        "+Nxs0NOJUjEoJ9wxH1xOUD6X5MDqDEL7PReFsIC2TIlwpi9j77ZyYi5SlYOYgY0RLNRzcw01fuxlXdA0DBUqgJcR1QoQ7iRp1p7uCS" +
        "/6ExLjU9P5dveIaF+3ir43xVgr11jp8GdE4NXFeGnPq5iKC88xpmwOMrGmJ4n2G8iI8pKu3Mlskup5mr1D/BWlWMPKIDUK4muQ6Sye2a5" +
        "/ANdT8GW0VGSNEIWm9ttQLyN7PAvzsN5c8SoRW1Stzd4jf5RDHeKzq/tZRMV/ClGTIO95v6LFRatWEiA5MLQPa/R" +
        "+zOyjymA1J77tG3aBgQA5ryFshYufQOORr6t/YtYpa5YuhdivG" +
        "/0pl0bgh3g7bnrjLKU9Eo1AspZGKqcWlYakpcLpa20cPvSkhuoXHnlitBmpDqPOs86ZApTDVwrDKjgZaCShj3V7RjNhw=",
        "http://textures.minecraft.net/texture/ca4da67aa051082646395b8dfffbe8ade09468ec95e17935c2af58c09c6ea86b"),

    /**
     * MineSkin (326705118)
     *
     * @link https://minesk.in/326705118
     */
    @SuppressWarnings("SpellCheckingInspection")
    BLUE("ewogICJ0aW1lc3RhbXAiIDogMTYwMzgwMjg3NzI0NCwKICAicHJvZmlsZUlkIiA6ICI1MGZiMTk1ZWIwOGM0YWY4OGJlNDk2Y2UyMDA2YjcwNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJhbHZpbjEyOSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jMDVmN2I2NDYzOTZiY2NjNGUyZjMxYWE4ZGM0OWZkMTQ5MDJmYzE4Mjg1NjJjMjk0NmM0NWFjNDNhYzE0OWUxIgogICAgfQogIH0KfQ==",
         "IL3lYbREKjFkDVMu48t3+LBcB9zkE2QyPjsy8PoiAMcJUxIuzxfoNMpR2rS1zGEIlpg39rlahgjbgETaVTvrAvB7fE7fLgI7ySFImGDhD" +
         "/GJXfI2ODBh9U1dOQk6/dlWv/0yapqX4k2JVXIYfALVTFNgMlck+0dnj78LRPYsMjGjv9JkPq9mmzbvN" +
         "/sYxbqKiO713yVseIp0ivBTv5rfi+nA4CR6L/TBpcrdKm83mdOe70zD4DI3TzkUQZ2uwEw5UYnuoJa13bJ6mjN0aoQHbkgOw0MQL" +
         "/mX089rid4unrwa3gJ7OefWKtmgoQuV0X6JyR34fqIBVa35PQ3BOJ9Y+1SIS0lUKr7jlcCzP+f6Vt0sbe7Yg" +
         "/uyPQaQdpQIlBJPyIiwTzoS8F9X208OmY9sLKNQRzUXOnhbK7U2g1VRR9dxh0IhjXUT9lDtTgFCt8BtGnfxYrZfQxIcx" +
         "/EifHTRof0h4btLO6rfhbIeuUyD3FYBXeiyCVhsN6EpES7gKDAeOWHEyEdomenoiZ8o2pbU+ZsZfqBixV4JaY8Nh23u" +
         "+d4f4w6Vi6Y4v2HHo5+rT2CEIaBbdkwPRdwH/TfL3bk2ZxW0VFBF08LGu" +
         "+6QQDBu0izmG4nomgieg9a5nnOeFF82D2ZNd92xem3f8R8rLpc7d1VpuH45WSkYGdl6iiCDwxs=",
         "http://textures.minecraft.net/texture/c05f7b646396bccc4e2f31aa8dc49fd14902fc1828562c2946c45ac43ac149e1"),

    /**
     * MineSkin (1867077022)
     *
     * @link https://minesk.in/1867077022
     */
    @SuppressWarnings("SpellCheckingInspection")
    GREEN("ewogICJ0aW1lc3RhbXAiIDogMTYwMzgwMjU4NjUwNSwKICAicHJvZmlsZUlkIiA6ICIzZmM3ZmRmOTM5NjM0YzQxOTExOTliYTNmN2NjM2ZlZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJZZWxlaGEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTY4OGRkYzY5NzNmYjdkM2MyNDZkMjg0ODczOTUxM2UzZGI5OTdmMTc0NDgxYmIwMWVkOWZkMDVkOTU3NTg5MyIKICAgIH0KICB9Cn0=",
          "FIwcJS5W5BaF5dYgrDHkD4aAC+C54npNKl8qHDmHgpunIWKCmTNfAcs1MjBU5M90Ashq7V1EH5KWulvcGBzFfRPo6xGsX/n0cXJZi8" +
          "/Lnt5jUAlysU6WHxPHZbfxzN6ur7PAJVXdVHR" +
          "+npvfOzCj6bKaAK7diI2jlKSujbAJ7fSbNs7WjFbtp3AqTpggHdOMO59kphGoV6GBgE8d0ddh0J7bhEbBanHzyNSKQW3FYfnqyTSEKRR9HMSUvaTrk+y6XHZKb/RmKGNdGf3HOhA5DHGnYqSwADxgv+m2Mz3R5rOwk6M7CpVLimSm7ttSw75b65dfWalCd7djdUCaZXxOlYIg9yivfZmB6VXqB3rnM22n6Oehg/JsHbZWLFo6nRAJCwQWgUx8impzca6cahsYx74AP120JRiSGq77lNrrYREyql3in5DdgC2VV+Rqqk5H2Zs/vJmh3LuKgbzL6gUb683BsWA3iik+TelyYkYTqIPcsYthzjQwT+Fy9eddwDdiglg07fIAdQnMmyy91ZV5eMtSteZAQ2GtKAnANuevA/bn8IdugjzqZQtNmwAL7LnpoBJGcM9Pz3gEyrzCWtw52ZXp/5uS+GaXeBr1D57igERJCoGJIn+tEbko3BUM/eSDiqWpn+7buX7bEv0GeN2jitQp7LXwmB15MrVo2eAMsZ0=",
          "http://textures.minecraft.net/texture/e688ddc6973fb7d3c246d2848739513e3db997f174481bb01ed9fd05d9575893"),

    /**
     * MineSkin (984313928)
     *
     * @link https://minesk.in/984313928
     */
    @SuppressWarnings("SpellCheckingInspection")
    PINK("ewogICJ0aW1lc3RhbXAiIDogMTYwMzgwMzM5NDM5MiwKICAicHJvZmlsZUlkIiA6ICIzZmM3ZmRmOTM5NjM0YzQxOTExOTliYTNmN2NjM2ZlZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJZZWxlaGEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmRiNWFiMTFiMjg3MDUzNjQzZTAwNmY1ZjViNzgyMzdlMTQxYjIyMTIwYzliNmUxYWFjOTVkZmNhOGY1NzFiZCIKICAgIH0KICB9Cn0=",
         "HP7lLDpneIAkAF1y0NNC3dGr2hywHva7Z/mTQv3yy1CkwUzgQsKh8ROAjhkXV3OEhFtpLRTtMUzHRgaYQ6EXwRJ0ntduK7L8cpZjoKn" +
         "+cRZzUOGJ4BRDfr4S+db2QsYbTrBo8bvFwoMnoJLnsIJH60z44ohDBXBEtRN7GWJHF695BswdOBLNlo40q" +
         "/tJo6iz1vAWY894x9J6yzmiqg2" +
         "+bNcUuzSUEf8vQAKfkqJlTAtG4VNxdCEjI4PIGmQbdrkohaJB4wEb8Ly3SZaIULe5h5Io6kkGntnSwQuSt5tcYXOLQqSspDzUSg10X" +
         "/jCtehJ95h9Ebp4zvXW9qZfC+6Z+O0PB1mdzWDAPLG+jt3WLql2G1+uCSPimnpFwPpihuGLpEa+i1QaqltCfW4Q10OEkd" +
         "/J0vHQm3w9wGWKspo3nQY1WePSd2B9GnSJ7F7s89D4m5wutQ6Jq5zA9+bsUjd0A4hknydahkSCBQ921Nuch7Bj53hEWjmTY" +
         "/1WvyriIVJY26J3woAVSA9stexXKcON3XYivX7IKLKJ3C2ouKJqluJE+Tzlryoun/7XGn7VJruydBpyDK449tRdaY/PBegQpJ" +
         "/sad2ATst3tWcpsmJRRYMiZHeOIrerMe9y+TBRTIQBmLAyBz6l1hZ7uOnhiABQ1Y3dly5c9ltdKZYviHIxtYY=",
         "http://textures.minecraft.net/texture/bdb5ab11b287053643e006f5f5b78237e141b22120c9b6e1aac95dfca8f571bd"),

    /**
     * MineSkin (923190146)
     *
     * @link https://minesk.in/923190146
     */
    @SuppressWarnings("SpellCheckingInspection")
    ORANGE("ewogICJ0aW1lc3RhbXAiIDogMTYwMzgwMzAwNjE0NCwKICAicHJvZmlsZUlkIiA6ICJmNjE1NzFmMjY1NzY0YWI5YmUxODcyMjZjMTEyYWEwYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJGZWxpeF9NYW5nZW5zZW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODc2ZGYxNDM4NWE2YTQwY2I3MjJmMTlmYzU0YjkyMTU4ZjVhMjQ5YWUwMTlmOTY2NDBlODQ4OWM0OWJiYjkyZiIKICAgIH0KICB9Cn0=",
           "vJnI7fxuqDg50WD6B+ITDsFrMuS3/nFQ+c2lcU57LYyUPPguHHwOcEqIUmQiyvXlwAUou77p/CtA5Gbp6h78PKMbJQlfs8" +
           "/BdRaGlTLzilAO2Q553DwfYb9IJcXOk+A3EbDocnV8VEBvV9Sede5SyShnLjUzsXtRYB1HVYhk2DRBd053RO52R6i6FHoEMW+eaGElCYs" +
           "+OxznFvbfZqTfyA4OWVQOnxLqQ5BLVJwozc0k9UFi0S2LFyj9XdmwRG6BDh8XEmxBOjeSydnlHy" +
           "/eiUGpjYedvuZ7FdVieqJPJbN3tZXCoZCz+s7vPRH7dSzvUz7V62p+gUbb/c/ku" +
           "+ZJ2NbEqUyuEF4cyUMkU3gMlsvORRS2o5GrUwGIyK5xobRH6xiibvffbc/Brr7CPvcPIWU" +
           "+b4KxVYIAiTQTwvlu1MGW1oJZiWfmmIf7VkIbxWhWoyLFiUyE2jnkVROJbclxVQ9orU" +
           "/UQZ1bra3an0qscJgoPi1oLQnlpfnILRwZpM2aq7M2fyV9VXB+XXsKBrNJuqr/f3ohhkpjLETst1tR1QlKst8yY+Ex/MRVn6EzlrxU" +
           "+osIeM6VCbrFcY4CKy4aKCHQ+sr1zNl7z+nbAiF5f8cGXfW9tnaTGP+yC2JxETZinIBS6IkTzO" +
           "+NsczMXJbPs6QiXG1ToqTFYOtcIfMSmiE=",
           "http://textures.minecraft.net/texture/876df14385a6a40cb722f19fc54b92158f5a249ae019f96640e8489c49bbb92f"),

    /**
     * MineSkin (644215007)
     *
     * @link https://minesk.in/644215007
     */
    @SuppressWarnings("SpellCheckingInspection")
    BLACK("ewogICJ0aW1lc3RhbXAiIDogMTYwMzgwMzgzMjU4NywKICAicHJvZmlsZUlkIiA6ICJjZGM5MzQ0NDAzODM0ZDdkYmRmOWUyMmVjZmM5MzBiZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJSYXdMb2JzdGVycyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yNTNlNTQ2YTdmNTY2YWRjOTA4ZDFjODRmNDk5ZGUwOTkzNTE3NTM1MTgzZWY5NDk1ZDY2M2Q5ZTExMTUxYjIzIgogICAgfQogIH0KfQ==",
          "JsVFb6eGjynudZj0prl2F+2aCdP74rnaLvmCQ+2+HauOHaUpDp1/PIF16QoGn5OU" +
          "/vsvq2eZxUICcd0XGEJQLwz1UB3j1hxqlfXjCCl5aiv0u/lKd8ePTE8N7Ea9M+5w+ufZHHTk/aNZUMyNuUS/ExpyZ+n" +
          "//ZWwbETLmhom0AsDtPTklZ9t/4fxHRa0c7rTGQdYJUBW1pL+vQeseve3FiZY1O2h+onAsP8kRd6ENg3KhHNUy0m/9ZRI0tILyYAhyW7" +
          "/7Q6H/ikd5pmWuFVYxIrdACd7AhSU4iSkkco+x8hVi3iWVpp2sqwHwZazcizP2QQPbYVYBzfTwHbb5V1+dBsdndX0K/DRE99Vki" +
          "/aWhIgFXfye9A6rZYAdwCUO5hEmf1Aizp9gb7ES7KxA0XGUZzlVwAbPna2IP65q/pJBIRnaii8z1phI8kKrQseaGn7Rv" +
          "/f6GKzaL6uSFrvA70p4OkjC9M1OHtNDRnpgG2c08yBeycC14yX4C09q5IXf3P6IRcyoxC0qmHuwymcd" +
          "+i9weZtypD3ctprpYATPaPODGzID1aETL20OSUgu8vXXdDGLdcbCXzqvwv1fcmpdv+lLSflDkvhI8oXxw" +
          "+bEWVvuYPe6QEunTaRWCEjh6tYzjmNF0aZvHy0+VfklWJQNugBxGjUGH7pIaKxz0nMtvzTtzk=",
          "http://textures.minecraft.net/texture/253e546a7f566adc908d1c84f499de0993517535183ef9495d663d9e11151b23");

    @Getter private final String textureData;
    @Getter private final String textureSignature;
    @Getter private final String textureURL;

    PlayerSkinOld(String textureData, String textureSignature, String textureURL) {
        this.textureData = textureData;
        this.textureSignature = textureSignature;
        this.textureURL = textureURL;
    }

    public GameProfile getGameProfile(UUID uuid, String name) {
        GameProfile gameProfile = new GameProfile(uuid, name);
        applyTextures(gameProfile);
        return gameProfile;
    }

    public void applyTextures(GameProfile gameProfile) {
        PropertyMap properties = gameProfile.getProperties();
        if (properties.containsKey("textures")) {
            properties.removeAll("textures");
        }

        properties.put("textures", getTexturesProperty());
    }

    public Property getTexturesProperty() {
        return new Property("textures", textureData, textureSignature);
    }

    public void changePlayerSkin(Player player) {
        GameProfile profile = ((CraftPlayer) player).getProfile();
        applyTextures(profile);
    }
}
