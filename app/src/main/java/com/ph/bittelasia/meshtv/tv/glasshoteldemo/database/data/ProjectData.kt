package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.data

import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item.*

class ProjectData {
    fun getAmenities(): List<AmenitiesItem> {
        val amenities = ArrayList<AmenitiesItem>()
        amenities.add(
            AmenitiesItem(
                "BREAKFAST",
                "amenities",
                "amenities for glas hotel",
                true,
                1,
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/food1",
                "logo for amenities"
            )
        )
        amenities.add(
            AmenitiesItem(
                "PARKING",
                "amenities",
                "amenities for glas hotel",
                true,
                1,
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/am2",
                "logo for amenities"
            )
        )
        amenities.add(
            AmenitiesItem(
                "INTERNET",
                "amenities",
                "amenities for glas hotel",
                true,
                1,
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/am3",
                "logo for amenities"
            )
        )
        amenities.add(
            AmenitiesItem(
                "FITNESS",
                "amenities",
                "amenities for glas hotel",
                true,
                1,
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/am4",
                "logo for amenities"
            )
        )
        amenities.add(
            AmenitiesItem(
                "POOL",
                "amenities",
                "amenities for glas hotel",
                true,
                2,
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/am5",
                "logo for amenities"
            )
        )
        return amenities
    }

    fun getMainUi(): List<MainUi> {
        val ui = ArrayList<MainUi>()
        ui.add(
            MainUi(
                1,
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/icon1",
                "TV",
                true
            )
        )
        ui.add(
            MainUi(
                2,
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/icon2",
                "MESSAGE",
                true
            )
        )
        ui.add(
            MainUi(
                3,
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/icon3",
                "AMENITIES",
                true
            )
        )
        return ui
    }

    fun getMessage(): List<MessagesItem> {
        val mess = ArrayList<MessagesItem>()
        mess.add(
            MessagesItem(
                1,
                "Pullman Hotel Team",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,",
                "09:00 AM",
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/messagebubble",
                "1",
                "Pullman Hotel Team",
            )
        )
        mess.add(
            MessagesItem(
                2,
                "Pullman Hotel Team",
                "when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
                "08:00 PM",
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/messagebubble",
                "0",
                "Purchase Receipt"
            )
        )
        mess.add(
            MessagesItem(
                3,
                "Pullman Hotel Team",
                " It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                "10:00 PM",
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/messagebubble",
                "0",
                "Happy Birthday Mr. Smith"
            )
        )
        mess.add(
            MessagesItem(
                4,
                "Pullman Hotel Team",
                "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old.",
                "07:00 PM",
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/messagebubble",
                "1",
                "Welcome!"
            )
        )
        mess.add(
            MessagesItem(
                5,
                "Pullman Hotel Team",
                "Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur",
                "5:00 PM",
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/messagebubble",
                "0",
                "House Keeping Mngmt"
            )
        )
        mess.add(
            MessagesItem(
                6,
                "Pullman Hotel Team",
                "Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil)",
                "2:00 PM",
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/messagebubble",
                "1",
                "Promo"
            )
        )
        mess.add(
            MessagesItem(
                7,
                "Pullman Hotel Team",
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters",
                "11:00 AM",
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/messagebubble",
                "1",
                "Management"

            )
        )
        return mess
    }
    fun getTv(): List<TvItem> {
        val tv = ArrayList<TvItem>()
        tv.add(
            TvItem(
                "1",
                "1",
                "NEWS"
            )
        )
        tv.add(
            TvItem(
                "2",
                "2",
                "SPORTS",
            )
        )
        tv.add(
            TvItem(
                "3",
                "3",
                "MOVIES",
            )
        )
        tv.add(
            TvItem(
                "4",
                "4",
                "KIDS",
            )
        )
        return tv
    }
    fun backgroundMode(): List<Bg> {
        val bg = ArrayList<Bg>()
        bg.add(
            Bg(
                1,
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/n1",
            )
        )
        bg.add(
            Bg(
                2,
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/n2",
            )
        )
        bg.add(
            Bg(
                3,
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/n3",
            )
        )
        bg.add(
            Bg(
                4,
                "android.resource://com.ph.bittelasia.meshtv.tv.glasshoteldemo/drawable/n4",
            )
        )
        return bg
    }
}