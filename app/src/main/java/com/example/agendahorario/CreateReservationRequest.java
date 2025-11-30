package com.example.agendahorario;

import com.google.gson.annotations.SerializedName;

public class CreateReservationRequest {

    @SerializedName("court_id")
    private int courtId;

    @SerializedName("start_datetime")
    private String startDatetime;

    @SerializedName("end_datetime")
    private String endDatetime;

    public CreateReservationRequest(int courtId, String startDatetime, String endDatetime) {
        this.courtId = courtId;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
    }

    public int getCourtId() {
        return courtId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }
}
