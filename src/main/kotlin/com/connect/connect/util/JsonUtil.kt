package com.connect.connect.util

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.io.IOException

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode
import java.io.InputStream
import java.util.*


class JsonUtil() {
    companion object {
        val mapper:ObjectMapper = ObjectMapper()
        init {
            mapper.registerModule(JavaTimeModule())
        }

        fun objectToJson(obj:Any):String{
            try {
                return mapper.writeValueAsString(obj)
            }catch (e:JsonProcessingException){
                throw ParseObjectToJsonException(e)
            }
        }

        fun <T> jsonToObject(json:String, valueType: Class<T>): T?{
            try {
                if(Objects.isNull(json)) return null
                return mapper.readValue(json, valueType)
            }catch (e:JsonParseException){
                throw ParseJsonToObjectException(e)
            }catch (e:JsonMappingException){
                throw JsonUtilMappingException(e)
            }catch (e:IOException){
                throw JsonUtilException(e)
            }
        }

        fun <T> jsonToObject(stream: InputStream, valueType: Class<T>): T?{
            try {
                if(Objects.isNull(stream)) return null
                return mapper.readValue(stream, valueType)
            }catch (e:JsonParseException){
                throw ParseJsonToObjectException(e)
            }catch (e:JsonMappingException){
                throw JsonUtilMappingException(e)
            }catch (e:IOException){
                throw JsonUtilException(e)
            }
        }


        fun <T> jsonToObject(stream: InputStream, valueType: TypeReference<T>): T?{
            try {
                if(Objects.isNull(stream)) return null
                return mapper.readValue(stream, valueType)
            }catch (e:JsonParseException){
                throw ParseJsonToObjectException(e)
            }catch (e:JsonMappingException){
                throw JsonUtilMappingException(e)
            }catch (e:IOException){
                throw JsonUtilException(e)
            }
        }

        fun <T> jsonToObject(json:String, valueType: TypeReference<T>): T?{
            try {
                if(Objects.isNull(json)) return null
                return mapper.readValue(json, valueType)
            }catch (e:JsonParseException){
                throw ParseJsonToObjectException(e)
            }catch (e:JsonMappingException){
                throw JsonUtilMappingException(e)
            }catch (e:IOException){
                throw JsonUtilException(e)
            }
        }

        fun jsonToJsonNode(json:String): JsonNode {
            try {
                return mapper.readValue(json, JsonNode::class.java)
            }catch (e:JsonParseException){
                throw ParseJsonToObjectException(e)
            }catch (e:JsonMappingException){
                throw JsonUtilMappingException(e)
            }catch (e:IOException){
                throw JsonUtilException(e)
            }
        }

    }
}