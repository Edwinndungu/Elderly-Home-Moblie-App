package com.example.nyumbayawazeehomecare.Member.Models;

public class HospitalAppointmentModel {
    private  String appointmentID;
    private  String name;
    private  String gender;
    private  String dob;
    private  String appointmentDate;
    private  String appointmentStatus;
    private  String appointmentDetails;
    private  String remarks;
    private  String careGiver;

    public HospitalAppointmentModel(String appointmentID,String name, String gender, String dob, String appointmentDate,
                                    String appointmentStatus, String appointmentDetails, String remarks, String careGiver) {
        this.appointmentID = appointmentID;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.appointmentDate = appointmentDate;
        this.appointmentStatus = appointmentStatus;
        this.appointmentDetails = appointmentDetails;
        this.remarks = remarks;
        this.careGiver = careGiver;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public String getAppointmentDetails() {
        return appointmentDetails;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getCareGiver() {
        return careGiver;
    }
}
