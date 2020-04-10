package com.belsoft.routes

import com.belsoft.utils.localPath
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
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
        val encoded = fileToByteArray(fileName)
        call.respond(encoded)
    }
}

suspend fun fileToByteArray(fileName: String?): ByteArray {
    val filesPath = "$localPath/resources/files/$fileName"
    return withContext(Dispatchers.IO){
        Files.readAllBytes(Paths.get(filesPath))
    }
}

fun Route.postFilesNamesArray(){
    post("/postFilesName"){
        val files = call.receive<List<String>>()
        val filesByteArray = mutableListOf<ByteArray>()
        for (file in files){
            val encoded = fileToByteArray(file)
            filesByteArray.add(encoded)
        }
        call.respond(filesByteArray)
    }
}