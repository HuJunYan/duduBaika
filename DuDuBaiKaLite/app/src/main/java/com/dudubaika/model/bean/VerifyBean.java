package com.dudubaika.model.bean;

import com.google.gson.annotations.SerializedName;

public class VerifyBean {

    private int time_used;
    private ResultRef1Bean result_ref1;
    private String request_id;

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public ResultRef1Bean getResult_ref1() {
        return result_ref1;
    }

    public void setResult_ref1(ResultRef1Bean result_ref1) {
        this.result_ref1 = result_ref1;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public static class ResultRef1Bean {

        private double confidence;
        private ThresholdsBean thresholds;

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public ThresholdsBean getThresholds() {
            return thresholds;
        }

        public void setThresholds(ThresholdsBean thresholds) {
            this.thresholds = thresholds;
        }

        public static class ThresholdsBean {

            @SerializedName("1e-3")
            private double _$1e3;
            @SerializedName("1e-5")
            private double _$1e5;
            @SerializedName("1e-4")
            private double _$1e4;
            @SerializedName("1e-6")
            private double _$1e6;

            public double get_$1e3() {
                return _$1e3;
            }

            public void set_$1e3(double _$1e3) {
                this._$1e3 = _$1e3;
            }

            public double get_$1e5() {
                return _$1e5;
            }

            public void set_$1e5(double _$1e5) {
                this._$1e5 = _$1e5;
            }

            public double get_$1e4() {
                return _$1e4;
            }

            public void set_$1e4(double _$1e4) {
                this._$1e4 = _$1e4;
            }

            public double get_$1e6() {
                return _$1e6;
            }

            public void set_$1e6(double _$1e6) {
                this._$1e6 = _$1e6;
            }
        }
    }
}
