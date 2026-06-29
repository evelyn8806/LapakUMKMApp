package edu.uph.m24si2.lapakumkmapp;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    public static List<EventModel> getAllEvents() {
        List<EventModel> list = new ArrayList<>();
        
        // --- BANDUNG ---
        list.add(new EventModel(
            "Festival Kuliner Nusantara",
            "Kuliner",
            "Alun-Alun Kota Bandung",
            "Rp. 250.000 / 3 hari",
            250000,
            R.drawable.festival_kuliner,
            "Nikmati hidangan lezat dari berbagai daerah di Indonesia.",
            "Bandung", -6.9217, 107.6071
        ));
        list.add(new EventModel(
            "Pasar Malam Tahun Baru",
            "Event Tahunan",
            "Lapangan Gasibu Bandung",
            "Rp. 200.000 / 5 hari",
            200000,
            R.drawable.pasar_malam,
            "Kemeriahan pasar malam menyambut tahun baru.",
            "Bandung", -6.9004, 107.6186
        ));

        // --- YOGYAKARTA ---
        list.add(new EventModel(
            "Festival Jajanan Pasar", 
            "Kuliner", 
            "Alun-Alun Utara Yogyakarta", 
            "Rp. 150.000 / Hari", 
            150000,
            R.drawable.festival_kuliner,
            "Nikmati berbagai jajanan pasar tradisional dari seluruh penjuru Jogja.",
            "Yogyakarta", -7.8014, 110.3644
        ));
        list.add(new EventModel(
            "Pasar Kangen Jogja", 
            "Bazar", 
            "Taman Budaya Yogyakarta", 
            "Rp. 50.000 / Hari", 
            50000,
            R.drawable.ic_shop,
            "Bernostalgia dengan jajanan dan barang lawas khas Yogyakarta.",
            "Yogyakarta", -7.7989, 110.3676
        ));

        // --- JAKARTA ---
        list.add(new EventModel(
            "Jakarta Sounds of Summer", 
            "Musik", 
            "JIExpo Kemayoran, Jakarta", 
            "Rp. 450.000 / Hari", 
            450000,
            R.drawable.festival_music,
            "Konser musik musim panas yang menghadirkan musisi-musisi ternama.",
            "Jakarta", -6.1497, 106.8456
        ));
        list.add(new EventModel(
            "Bazar Buku Murah", 
            "Bazar", 
            "Istora Senayan, Jakarta", 
            "Rp. 30.000 / Hari", 
            30000,
            R.drawable.ic_shop,
            "Temukan jutaan buku dengan harga sangat terjangkau.",
            "Jakarta", -6.2297, 106.8456
        ));

        return list;
    }

    public static List<EventModel> getEventsByCity(String city) {
        List<EventModel> filtered = new ArrayList<>();
        for (EventModel event : getAllEvents()) {
            if (event.getKota().equalsIgnoreCase(city)) {
                filtered.add(event);
            }
        }
        return filtered;
    }
}
