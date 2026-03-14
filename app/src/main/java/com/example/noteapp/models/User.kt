package com.example.noteapp.models

import java.math.BigInteger

class User {
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
