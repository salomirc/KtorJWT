package com.belsoft.templates

import com.belsoft.baseURL
import kotlinx.html.*

fun HTML.crudCreateTableTemplate(title: String, pathParam: String, data: List<Map<String, String>>){
    head {
        title { +"StarWars films list from Db" }
        styleLink("/static/crud.css")
    }
    body {
        h1("page_title"){
            + "Index"
        }
        a(href = "/admin/$pathParam/create", classes = "blue_button_link"){
            + "Create New"
        }
        div("space30px"){

        }
        div {
            table("data") {
                tr {
                    for (key in data[0].keys) {
                        th { +key }
                    }
                }
                for (items in data){
                    tr {
                        for ((k, v) in items) {
                            td { +v}
                        }
                        td {
                            a("admin/$pathParam/edit/${items["id"]}", classes = "crud_link"){
                                +"Edit"
                            }
                            +" | "
                            a("/admin/$pathParam/details/${items["id"]}", classes = "crud_link"){
                                +"Details"
                            }
                            +" | "
                            a("/admin/$pathParam/delete/${items["id"]}", classes = "crud_link"){
                                +"Delete"
                            }
                        }
                    }
                }
            }
        }
    }
}