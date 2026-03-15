package com.example.noteapp.data.database

class NoteDatabase {
    constructor()
    constructor(id:Int,name: String){
        this.id = id;
        this.name = name;
    }
    var id:Int = 0;
    var name:String = "";
    var password:String = "";
    var email:String = "";
    var phoneNo:String = "";

}
