package jti.jasminsa.storyapp

import com.google.gson.annotations.SerializedName
import jti.jasminsa.storyapp.data.response.ListStoryItem
import java.util.Random
import kotlin.text.Typography.quote

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
                val story = ListStoryItem(
                    Random().nextDouble() * 100,
                    Random().nextDouble() * 100,
            "photoUrl $i",
            "createdAt $i",
                    "name $i",
            "description $i",
                    "id $i",
            )
            items.add(story)
        }
        return items
    }
}