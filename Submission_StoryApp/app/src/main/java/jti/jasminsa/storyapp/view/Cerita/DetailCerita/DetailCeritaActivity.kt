package jti.jasminsa.storyapp.view.Cerita.DetailCerita

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import jti.jasminsa.storyapp.R
import jti.jasminsa.storyapp.data.response.ListStoryItem
import jti.jasminsa.storyapp.databinding.ActivityDaftarCeritaBinding
import jti.jasminsa.storyapp.databinding.ActivityDetailCeritaBinding
import jti.jasminsa.storyapp.view.Cerita.DaftarCerita.DaftarCeritaViewModel

class DetailCeritaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailCeritaBinding

    companion object {
        const val DETAIL = "detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCeritaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detail = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(DETAIL, ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(DETAIL)
        }
        if(detail != null){
            Glide.with(this)
                .load(detail.photoUrl) // URL Gambar
                .into(binding.imageView2)
            binding.tvName.text = detail.name
            binding.tvdes.text = detail.description
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}