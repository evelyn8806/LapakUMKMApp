package edu.uph.m24si2.lapakumkmapp;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    public static List<EventModel> getAllEvents() {
        List<EventModel> list = new ArrayList<>();
        
        list.add(new EventModel(
            "Festival Jajanan Pasar", 
            "Kuliner", 
            "Alun-Alun Utara Yogyakarta", 
            "Rp. 150.000 / Hari", 
            R.drawable.festival_kuliner,
            "Nikmati berbagai jajanan pasar tradisional dari seluruh penjuru Jogja."
        ));

        list.add(new EventModel(
            "Pasar Malam Rakyat", 
            "Hiburan", 
            "Lapangan Puputan Renon, Denpasar", 
            "Rp. 100.000 / Hari", 
            R.drawable.pasar_malam,
            "Hiburan keluarga dengan berbagai wahana permainan dan stand UMKM lokal."
        ));

        list.add(new EventModel(
            "Festival Kuliner Nusantara",
            "Kuliner",
            "Alun-Alun Kota Bandung",
            "Rp. 250.000 / 3 hari",
            R.drawable.festival_kuliner,
            "Nikmati hidangan lezat dari berbagai daerah di Indonesia."
        ));

        list.add(new EventModel(
            "Jakarta Sounds of Summer", 
            "Musik", 
            "JIExpo Kemayoran, Jakarta", 
            "Rp. 450.000 / Hari", 
            R.drawable.festival_music,
            "Konser musik musim panas yang menghadirkan musisi-musisi ternama."
        ));

        return list;
    }
}