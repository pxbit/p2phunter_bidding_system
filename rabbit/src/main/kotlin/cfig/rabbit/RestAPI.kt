package cfig.rabbit

class RestAPI {
    data class MessageItme(var messages_details: String)

    data class MessageDetails(var node: String,
                              var name: String,
                              var messages_ready_ram: String)
}