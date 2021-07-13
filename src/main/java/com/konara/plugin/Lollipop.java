package com.konara.plugin;

public enum Lollipop{
        Q1("Konara_moe",2803530989L),
        Q2("bingling_sama",2082152212L),
        Q3("hiyakuya",1842105028L),
        Q4("beanflame",2962672241L);




        private String RealName;
        private long QQ;
        Lollipop(String RealName, Long QQ){
            this.RealName=RealName;
            this.QQ=QQ;
        }
        public static String getName(long QQ) {
            for (Lollipop l : Lollipop.values()) {
                if (l.getQQ() == QQ) {
                    return l.RealName;
                }
            }
            return null;
        }
        public long getQQ() {
            return QQ;
        }
}
