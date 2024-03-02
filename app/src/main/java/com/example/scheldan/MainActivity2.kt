package com.example.scheldan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import com.example.scheldan.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity(),PostAdapter.Listener {
   private val viewModel: PostViewModel by viewModels()
    private lateinit var binding:ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = PostAdapter(this)
        binding.recycle.adapter = adapter
        viewModel.data.observe(this){posts ->
            adapter.submitList(posts)
        }
       binding.btncontent.setOnClickListener {
           with(binding.editText){
               if (text.isNullOrBlank()) {
                   Toast.makeText(
                       this@MainActivity2,
                               "Content cant be empty",
                       Toast.LENGTH_SHORT
                   ).show()
                   return@setOnClickListener
               }
               viewModel.changeContent(text.toString())
               viewModel.save()
               setText("")
               clearFocus()
               AndroidUtils.hideKeyboard(this)
           }
       }

    }
    override  fun onClickLike(post: Post){
        viewModel.likeById(post.id)
    }
    override fun onClickShare(post: Post){
        viewModel.shareById(post.id)
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, post.content)
        }
        val shareIntent = Intent.createChooser(intent, "Выберите прогу")
        startActivity(shareIntent)
    }

    override fun onClickMore(post: Post, view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu_more, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            if (it.itemId == R.id.popup_delete){
                viewModel.removeById(post.id)
            }
            if (it.itemId == R.id.popup_edit){
                viewModel.editById(post.id,binding.editText.text.toString())
            }
            true
        }
        popupMenu.show()
    }



    override fun onClickEdit(post: Post, view: View) {
        TODO("Not yet implemented")
    }


    override fun onClickRemove(post: Post) {
        TODO("Not yet implemented")
    }

}






