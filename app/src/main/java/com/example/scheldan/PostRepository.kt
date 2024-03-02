package com.example.scheldan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface PostRepository {
    fun  getAll(): LiveData<List<Post>>
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun likeById(id:Long)
    fun save(post: Post)
    fun editById(id: Long, content: String)
}
 class PostRepositoryInMemoryImpl : PostRepository {
     private var Posts = listOf(
         Post(
             id = 2,
             author = "BTPIT36",
             content = "Да я ведь все сам!!! Все своими руками… Дом построил, сам! Вот этими вот руками. Сад посадил сам, своими руками, машину отремонтировал… Все САМ!!! Все своими руками… — Да, ты небось и не женат. — Нет, все сам, все своими руками",
             published = "21 may 18:36",
             likedByMe = false,
             amountlike = 999,
             sharecount = 999,
             link = "https://youtu.be/RQ9uBKo328s?si=zniOsCIW2nkl4EJv",
         ),
         Post(
             id = 1,
             author = "BMW",
             content = "Автомобили M BMW 5 серии впечатляющим образом сочетают в себе фирменную спортивность BMW M с комфортом и элегантностью седана бизнес-класса. Познакомьтесь с тремя уникальными автомобилями BMW M с яркими характерами. Быстрейший в истории, новый BMW M5 CS с двигателем мощностью в 635 л.с. (467 кВт) и разгоном до 100 км/ч за рекордные 3 секунды",
             published = "22 may 12:00",
             likedByMe = false,
             amountlike = 999,
             sharecount = 999,
             link = "https://youtu.be/3FPkKHQIXL8?si=XhhM9YSvGFDjcEao",
         ),

         )


     private val data = MutableLiveData(Posts)
     override fun getAll(): LiveData<List<Post>> = data
     override fun likeById(id: Long) {
         Posts = Posts.map {
             if (it.id != id) it
             else {
                 if (it.likedByMe)
                     it.amountlike--
                 else
                     it.amountlike++
                 it.copy(likedByMe = !it.likedByMe)
             }

         }
         data.value = Posts
     }

     override fun editById(id: Long, content: String) {
         Posts = Posts.map {
             if(it.id != id)
                 it
             else {
                 if (it.id == 0L ) it.id = nextId(Posts).toLong()
                 it.copy(content = content)
             }
         }
         data.value = Posts
     }



     override fun save(post: Post) {
         if (post.id == 0L) {
             Posts = listOf(
                 post.copy(
                     id = nextId(Posts).toLong(),
                     author = "Крымский движняк",
                     likedByMe = false,
                     published = "завтра",
                     amountlike = 0,
                     sharecount = 0
                 )
             ) + Posts
             data.value = Posts
             return
         }


         Posts = Posts.map {


             if (it.id != post.id) it else it.copy(content = post.content)
         }
         data.value = Posts
     }

     override fun shareById(id: Long) {
         Posts = Posts.map {
             if (it.id != id) it else
                 it.copy(sharecount = it.sharecount + 1)
         }
         data.value = Posts
     }

     override fun removeById(id: Long) {
         Posts = Posts.filter { it.id != id }
         data.value = Posts
     }
 }

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    sharecount = 0,
    amountlike = 0,
    link = "",
)
class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    val editText = MutableLiveData(empty)
    fun save(){
        editText.value?.let {
            repository.save(it)
        }
        editText.value = empty
    }
    fun changeContent(content: String){
        editText.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            editText.value = it.copy(content = text)
        }
    }
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun editById(id: Long, content:String){
        repository.editById(id,content)
    }
}
fun nextId(posts:List<Post>):Int{
    var id = 1
    posts.forEach{it1->
        posts.forEach{
            if (it.id.toInt()==id) id=(it.id+1).toInt()
        }
    }

    return id
}