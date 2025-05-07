    import kotlinx.coroutines.*
    import java.io.File
    import java.util.*
    import javax.activation.DataHandler
    import javax.activation.FileDataSource
    import javax.mail.*
    import javax.mail.internet.*

    class EmailSender(private val userEmail: String, private val userPassword: String) {

        suspend fun sendEmailWithAttachment(toEmail: String, subject: String, messageBody: String, file: File): Boolean {
            return withContext(Dispatchers.IO) {
                val properties = Properties()
                properties["mail.smtp.auth"] = "true"
                properties["mail.smtp.starttls.enable"] = "true"
                properties["mail.smtp.host"] = "smtp.gmail.com"
                properties["mail.smtp.port"] = "587"

                val session = Session.getInstance(properties, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(userEmail, userPassword)
                    }
                })

                return@withContext try {
                    val message = MimeMessage(session)
                    message.setFrom(InternetAddress(userEmail))
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
                    message.subject = subject

                    val mimeBodyPart = MimeBodyPart()
                    mimeBodyPart.setContent(messageBody, "text/html; charset=utf-8")

                    val attachmentBodyPart = MimeBodyPart()
                    val dataSource = FileDataSource(file)
                    attachmentBodyPart.dataHandler = DataHandler(dataSource)
                    attachmentBodyPart.fileName = file.name

                    val multipart = MimeMultipart()
                    multipart.addBodyPart(mimeBodyPart)
                    multipart.addBodyPart(attachmentBodyPart)

                    message.setContent(multipart)

                    Transport.send(message)
                    true
                } catch (e: MessagingException) {
                    e.printStackTrace()
                    false
                }
            }
        }

        fun sendEmail(toEmail: String, subjectText: String, messageBody: String): Boolean {
            return try {
                val properties = Properties().apply {
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                    put("mail.smtp.host", "smtp.gmail.com")
                    put("mail.smtp.port", "587")
                    put("mail.smtp.ssl.trust", "smtp.gmail.com")
                }

                val session = Session.getInstance(properties, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication("organizerclasses@gmail.com", "vxtu zkbx qmuw ayrn") // Use App Password
                    }
                })

                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress("organizerclasses@gmail.com"))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
                    subject = subjectText  // ✅ FIX: Using subjectText instead of subject
                    setContent(messageBody, "text/html; charset=utf-8")
                }

                Transport.send(message)
                true
            } catch (e: MessagingException) {
                e.printStackTrace()
                false
            }
        }
    }
