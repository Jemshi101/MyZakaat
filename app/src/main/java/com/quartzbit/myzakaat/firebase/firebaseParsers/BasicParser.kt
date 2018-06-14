package `com`.quartzbit.myzakaat.firebase.firebaseParsers

import com.google.firebase.database.DataSnapshot

import java.util.ArrayList

/**
 * Created by Jemsheer K D on 16 August, 2017.
 * Package com.quartzbit.myzakaat.firebase.firebaseParsers
 * Project Dearest
 */

open class BasicParser {

    fun getStringList(dataSnapshot: DataSnapshot): ArrayList<String> {
        val list = ArrayList<String>()
        if (dataSnapshot.exists()) {
            for (snapshot in dataSnapshot.children) {
                list.add(snapshot.key.toString())
            }
        }
        return list
    }

    fun getStringValueList(dataSnapshot: DataSnapshot): ArrayList<String> {
        val list = ArrayList<String>()
        if (dataSnapshot.exists()) {
            for (snapshot in dataSnapshot.children) {
                try {
                    list.add("" + snapshot.value!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        return list
    }

    fun getStringBooleanValueMap(dataSnapshot: DataSnapshot): LinkedHashMap<String, Boolean> {
        val map = LinkedHashMap<String, Boolean>()
        if (dataSnapshot.exists()) {
            for (snapshot in dataSnapshot.children) {
                try {
                    map.put(snapshot.key.toString(), snapshot.value as Boolean)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        return map
    }

    fun getStringIntValueMap(dataSnapshot: DataSnapshot): LinkedHashMap<String, Int> {
        val map = LinkedHashMap<String, Int>()
        if (dataSnapshot.exists()) {
            for (snapshot in dataSnapshot.children) {
                try {
                    map.put(snapshot.key.toString(), snapshot.value as Int)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        return map
    }

    fun getURLList(dataSnapshot: DataSnapshot): ArrayList<String> {
        val list = ArrayList<String>()
        if (dataSnapshot.exists()) {
            for (snapshot in dataSnapshot.children) {
                list.add(getString(snapshot, "url"))
            }
        }
        return list
    }

    fun getString(dataSnapshot: DataSnapshot, field: String): String {
        return if (dataSnapshot.child(field).exists()) {
            try {
                "" + dataSnapshot.child(field).value!!
            } catch (e: Exception) {
                ""
            }

        } else {
            ""
        }
    }

    fun getLong(dataSnapshot: DataSnapshot, field: String): Long {
        return if (dataSnapshot.child(field).exists()) {
            try {
                java.lang.Long.parseLong("" + dataSnapshot.child(field).value!!)
            } catch (e: NumberFormatException) {
                0L
            }
        } else {
            0L
        }
    }

    fun getLong(dataSnapshot: DataSnapshot): Long {
        try {
            return java.lang.Long.parseLong("" + dataSnapshot.value!!)
        } catch (e: NumberFormatException) {
            return 0L
        }

    }

    fun getInt(dataSnapshot: DataSnapshot, field: String): Int {
        return if (dataSnapshot.child(field).exists()) {
            try {
                Integer.parseInt("" + dataSnapshot.child(field).value!!)
            } catch (e: NumberFormatException) {
                0
            }

        } else {
            0
        }
    }

    fun getInt(dataSnapshot: DataSnapshot): Int {
        return if (dataSnapshot.exists()) {
            try {
                Integer.parseInt("" + dataSnapshot.value!!)
            } catch (e: NumberFormatException) {
                0
            }

        } else {
            0
        }
    }

    fun getDouble(dataSnapshot: DataSnapshot, field: String): Double {
        return if (dataSnapshot.child(field).exists()) {
            try {
                java.lang.Double.parseDouble("" + dataSnapshot.child(field).value!!)
            } catch (e: NumberFormatException) {
                0.0
            }

        } else {
            0.0
        }
    }

    fun getFloat(dataSnapshot: DataSnapshot, field: String): Float {
        return if (dataSnapshot.child(field).exists()) {
            try {
                java.lang.Float.parseFloat("" + dataSnapshot.child(field).value!!)
            } catch (e: NumberFormatException) {
                0f
            }

        } else {
            0f
        }
    }

    fun getBoolean(dataSnapshot: DataSnapshot, field: String): Boolean {
        return if (dataSnapshot.child(field).exists()) {
            try {
                dataSnapshot.child(field).value as Boolean
            } catch (e: Exception) {
                false
            }

        } else {
            false
        }
    }

    fun getPhotoPath(dataSnapshot: DataSnapshot, field: String): String {
        return if (dataSnapshot.child(field).exists()) {
            try {
                var str = "" + dataSnapshot.child(field).value!!
                if (str.startsWith("/public/")) {
                    str = str.substring("/public/".length)
                } else if (str.startsWith("public/")) {
                    str = str.substring("public/".length)
                }
                str
            } catch (e: Exception) {
                ""
            }
        } else {
            ""
        }
    }

    fun getPhotoPath(dataSnapshot: DataSnapshot): String {
        return if (dataSnapshot.exists()) {
            try {
                var str = "" + dataSnapshot.value!!
                if (str.startsWith("/public/")) {
                    str = str.substring("/public/".length)
                } else if (str.startsWith("public/")) {
                    str = str.substring("public/".length)
                }
                str
            } catch (e: Exception) {
                ""
            }
        } else {
            ""
        }
    }

}
