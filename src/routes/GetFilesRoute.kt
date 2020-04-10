package com.belsoft.routes

import com.belsoft.utils.localPath
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun Route.getStaticFilesName(){
    get("/getStaticFilesName"){
        val staticPath = "$localPath/resources/static"
        listPathFiles(staticPath, this)
    }
}

fun Route.getPrivateFilesName(){
    get("/getPrivateFilesName"){
        val staticPath = "$localPath/resources/files"
        listPathFiles(staticPath, this)
    }
}

suspend fun listPathFiles(path: String, pipelineContext: PipelineContext<Unit, ApplicationCall>){
    val listing = File(path).walkTopDown().toList()
    val filesName = mutableListOf<String>()
    if (listing.isNotEmpty()){
        val filesList = listing[0].listFiles()
        filesList?.let {
            filesList.forEach {
                filesName.add(it.name)
            }
        }
    }
    filesName.sort()
    pipelineContext.call.respond(filesName)
}

fun Route.getFileAsByteArray(){
    get("/getFile/{fileName}"){
        val fileName = call.parameters["fileName"]
        val filesPath = "$localPath/resources/files/$fileName"
        val encoded = withContext(Dispatchers.IO){
            Files.readAllBytes(Paths.get(filesPath))
        }
        call.respond(encoded)
    }
}