package com.example.nafisquaisarcoachingcenter.coursecclass

import android.content.ContentValues
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.nafisquaisarcoachingcenter.databinding.ActivityCourseOpenBinding
import com.example.nafisquaisarcoachingcenter.model.UserData
import com.example.nafisquaisarcoachingcenter.model.userDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.nafisquaisarcoachingcenter.R
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfName.Line
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.layout.properties.VerticalAlignment
import java.io.ByteArrayOutputStream


class CourseOpenActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityCourseOpenBinding
    private var userData: UserData? = null
    private var courseName: String? = null
    private var courseId: String? = null
    private var courseDesc: String? = null
    private var offerAmount: String? = null
    private var  isBuy: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseOpenBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        // Fetch user data
        fetchUserData { fetchedData ->
            userData = fetchedData
            if (userData == null) {
                Log.e("UserData", "Failed to fetch user data.")
                Toast.makeText(this, "Unable to load user data.", Toast.LENGTH_SHORT).show()
            }
        }

        // Get course details from intent
        courseId = intent.getStringExtra("courseId")
        courseName = intent.getStringExtra("courseName")
        courseDesc = intent.getStringExtra("courseDesc")
        val courseAmount = intent.getStringExtra("courseAmount")
        offerAmount = intent.getStringExtra("offerAmount")
        val courseImgUrl = intent.getStringExtra("courseImgUrl")
        isBuy  = intent.getBooleanExtra("isBuy", false)
        val courseDateTimestamp = intent.getLongExtra("courseDate", 0L)
        val courseDate = if (courseDateTimestamp != 0L) Date(courseDateTimestamp) else null

        binding.tollbarTitle.text = courseName


        // Check purchase state from Firebase
        checkIsBuy(courseId!!) { isPurchased ->
            isBuy = isPurchased
            Log.d("IsBuy", isBuy.toString())
            binding.courseBuyBtn.text = if (isBuy) "Explore" else "Buy"
            binding.courseBuyBtn.setOnClickListener {
                if (isBuy) {
                    val intent = Intent(this@CourseOpenActivity, ClassMainActivity::class.java)
                    intent.putExtra("courseId", courseId)
                    intent.putExtra("courseName", courseName)
                    startActivity(intent)

                } else {
                    startPayment()
                }
            }

        }

        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val formattedDate = courseDate?.let { dateFormat.format(it) }
        val courseEndDate = getCourseEndDate(courseDate)

        // Set course details in UI
        setAllDetail(
            courseName, courseDesc, courseAmount, offerAmount, courseImgUrl,
            formattedDate, courseEndDate, courseId, isBuy
        )


        // Back button listener
        binding.backarrowbtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun checkIsBuy(courseId: String, callback: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId.isNullOrEmpty()) {
            callback(false)  // No user logged in, return false
            return
        }

        val paymentRef = FirebaseFirestore.getInstance()
            .collection("UserPurchase")
            .document(userId)
            .collection("Course")
            .document(courseId)
            .collection("Payments")
            .whereEqualTo("status", "Success")
            .get()

        paymentRef.addOnSuccessListener { querySnapshot ->
            // Check if any document with status = "Success" exists
            val isBuy = querySnapshot.documents.any { it.getString("paymentId") != null }
            callback(isBuy)  // Pass the result to the callback
        }
        paymentRef.addOnFailureListener {
            // Handle failure (e.g., network error)
            callback(false)  // Return false if the query fails
        }
    }

    private fun getCourseEndDate(courseDate: Date?): String {
        val calendar = Calendar.getInstance()
        calendar.time = courseDate ?: Date()
        calendar.add(Calendar.YEAR, 1)

        val lastDateAfterOneYear = calendar.time
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        return dateFormat.format(lastDateAfterOneYear)
    }

    private fun setAllDetail(
        courseName: String?,
        courseDesc: String?,
        courseAmount: String?,
        offerAmount: String?,
        courseImgUrl: String?,
        courseDate: String?,
        courseEndDate: String,
        courseId: String?,
        isBuy: Boolean
    ) {
        binding.apply {
            CourseDate.text = courseDate ?: "N/A"
            CourseDescription.text = courseDesc
            CourseTitle.text = courseName
            showPriceOfCourse.text = "₹ $courseAmount"
            showOfferPriceOfCourse.text = "₹ $offerAmount"
            courseDuration.text = "$courseDate to $courseEndDate"
            showPriceOfCourse.paintFlags = showPriceOfCourse.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            Glide.with(this@CourseOpenActivity)
                .load(courseImgUrl)
                .into(CourseImg)


        }
    }

    private fun startPayment() {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_FR7kRImOW3Zhxq")

        try {
            val amountInInt = offerAmount?.toIntOrNull()?.times(100) ?: 0 // Safely convert string to int and multiply by 100
            if (amountInInt <= 0) {
                Toast.makeText(this, "Invalid offer amount", Toast.LENGTH_SHORT).show()
                return
            }

            val options = JSONObject().apply {
                put("name", "Organizer Classes")
                put("description", "This is for best Classes")
                put("image", "https://firebasestorage.googleapis.com/v0/b/nafis-coaching-center.appspot.com/o/Logo%2Forganizer_icon.png?alt=media&token=9ebd30b1-b08c-43d6-8aa2-916903605642") // Replace with your hosted image URL
                put("currency", "INR")
                put("theme.color", "#34A671");
                put("amount", amountInInt) // Amount in paise
                val prefill = JSONObject().apply {
                    put("email", userData?.email)
                    put("contact", userData?.number)
                }
                put("prefill", prefill)
                val retryObj = JSONObject().apply {
                    put("enabled", false)
                    put("max_count", 4)
                }
                put("retry", retryObj)
            }

            checkout.open(this, options)
        } catch (e: Exception) {
            Log.e("RazorpayError", "Error in starting Razorpay Checkout", e)
        }
    }

    private fun fetchUserData(onComplete: (UserData?) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.e("fetchUserData", "User is not logged in.")
            onComplete(null)
            return
        }

        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(userDetail::class.java)
                user?.let {
                    val fetchedData = UserData(
                        name = it.Name,
                        email = it.Email,
                        number = it.Number,
                        profilePic = it.ProUrl
                    )
                    Log.d("fetchUserData", "Fetched Data: $fetchedData")
                    onComplete(fetchedData)
                } ?: run {
                    Log.e("fetchUserData", "User data is null.")
                    onComplete(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("fetchUserData", "Error fetching data: ${error.message}")
                onComplete(null)
            }
        })
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        Log.d(ContentValues.TAG, "Payment Successful: $razorpayPaymentId")
        Toast.makeText(this, "Payment Successful: $razorpayPaymentId", Toast.LENGTH_SHORT).show()

        saveBuy(razorpayPaymentId)

        val pdfPath = userData?.let {
            generatePDF(
                paymentId = razorpayPaymentId ?: "N/A",
                userEmail = it.email,
                userName = it.name,
                userNumber = it.number,
                amount = offerAmount!!
            )
        }

        pdfPath?.let { openPDF(it) }

//        userData?.email?.let { email ->
//            pdfPath?.let { sendEmailWithPDF(email, it) }
//        }

        isBuy = true
        binding.courseBuyBtn.text = "Explore"

        // Optionally, re-check Firebase to confirm the purchase state
        checkIsBuy(courseId!!) { isPurchased ->
            if (isPurchased) {
                isBuy = true
                binding.courseBuyBtn.text = "Explore"
            }
        }
    }
    private fun generatePDF(
        paymentId: String,
        userEmail: String,
        userName: String,
        userNumber: String,
        amount: String,
    ): String {
        val pdfPath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
        val directory = File(pdfPath)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, "PaymentReceipt_$paymentId.pdf")

        try {
            val writer = PdfWriter(file)
            val pdfDocument = PdfDocument(writer)

            // Add Metadata
            pdfDocument.documentInfo.apply {
                title = "Payment Receipt"
                author = "Organizer Classes"
                subject = "Payment Confirmation"
                keywords = "Payment, Receipt, Organizer Classes"
                creator = "Organizer App"
            }

            val document = Document(pdfDocument)

            // Load Logo from Drawable PNG
            val logoBitmap = BitmapFactory.decodeResource(resources, R.drawable.organizer_icon) // Replace 'organizer_icon' with your drawable PNG name
            val byteArrayOutputStream = ByteArrayOutputStream()
            logoBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val logoImageData = ImageDataFactory.create(byteArrayOutputStream.toByteArray())

            // Create a table with 2 columns
            val table = Table(floatArrayOf(1f, 3f)) // Adjust column widths as needed
            table.setWidth(UnitValue.createPercentValue(100f))

            // Add Logo to the left column
            val logo = Image(logoImageData)
                .setWidth(40f) // Increased width for better visibility
                .setHeight(40f) // Adjust height proportionally
                .setAutoScale(true)

            table.addCell(
                Cell().add(logo)
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
            )

            // Add Organizer Classes and Payment Receipt to the right column
            val rightText = Paragraph("Organizer Classes")
                .setFontSize(30f)
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT)

            val receiptText = Paragraph("Payment Receipt")
                .setFontSize(20f)
                .setTextAlignment(TextAlignment.RIGHT)

            val rightCell = Cell()
                .add(rightText)
                .add(receiptText)
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPaddingRight(10f) // Add padding to move text closer to the right edge

            table.addCell(rightCell)

            // Add the table to the document
            document.add(table)

            // Add Horizontal Line
            val line = LineSeparator(SolidLine())
            line.setMarginTop(10f).setMarginBottom(10f)
            document.add(line)

            // Add Course and Payment Details
            document.add(Paragraph("Course Name: $courseName").setMarginBottom(10f))
            document.add(Paragraph("Description: Hope you enjoy the Course").setMarginBottom(5f))
            document.add(Paragraph("Amount Paid: ₹$amount").setMarginBottom(10f))
            document.add(Paragraph("User Name: $userName").setMarginBottom(5f))
            document.add(Paragraph("Email: $userEmail").setMarginBottom(5f))
            document.add(Paragraph("Phone Number: $userNumber").setMarginBottom(10f))
            document.add(Paragraph("Payment ID: $paymentId").setMarginBottom(5f))
            document.add(
                Paragraph(
                    "Date: ${
                        SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
                            .format(System.currentTimeMillis())
                    }"
                ).setMarginBottom(10f)
            )

            // Add another horizontal line
            val secondLine = LineSeparator(SolidLine())
            secondLine.setMarginTop(10f).setMarginBottom(10f)
            document.add(secondLine)

            // Add Footer
            document.add(
                Paragraph("Thank you for your payment!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(16f)
                    .setMarginBottom(20f)
            )
            document.add(
                Paragraph("For support, contact us at organizerclasses.com")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12f)
            )

            document.close() // Close Document First
            pdfDocument.close() // Then Close PdfDocument

            Log.d("PDF", "PDF generated successfully at ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e("PDF", "Error while generating PDF", e)
        }

        return file.absolutePath
    }








    private fun openPDF(filePath: String) {
        val file = File(filePath)
        val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(intent, "Open PDF"))
    }

    private fun sendEmailWithPDF(email: String, pdfPath: String) {
        val file = File(pdfPath)
        val uri = FileProvider.getUriForFile(
            this,
            "$packageName.provider",
            file
        )

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, "Payment Receipt")
            putExtra(Intent.EXTRA_TEXT, "Dear user,\n\nPlease find your payment receipt attached.")
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using"))
            Log.d("Email", "Email sent successfully.")
        } catch (e: Exception) {
            Log.e("Email", "Error while sending email", e)
        }
    }


    private fun saveBuy(razorpayPaymentId: String?) {
        if (razorpayPaymentId.isNullOrEmpty()) {
            Log.e("Payment", "Invalid Razorpay Payment ID")
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId.isNullOrEmpty()) {
            Log.e("Payment", "User is not logged in.")
            return
        }

        if (courseId.isNullOrEmpty()) {
            Log.e("Payment", "Course ID is null.")
            return
        }

        // Ensure userData is valid
        if (userData == null) {
            Log.e("Payment", "User data is null. Cannot save payment.")
            return
        }

        val paymentId = getPaymentId(userData?.name.orEmpty())
        val paymentRef = FirebaseFirestore.getInstance()
            .collection("UserPurchase")
            .document(userId)
            .collection("Course")
            .document(courseId!!)
            .collection("Payments")
            .document(paymentId)

        // Create a map with payment details
        val paymentData = hashMapOf(
            "paymentId" to razorpayPaymentId,
            "userId" to userId,
            "userEmail" to userData?.email.orEmpty(),
            "userName" to userData?.name.orEmpty(),
            "courseId" to courseId,
            "amount" to 500,
            "status" to "Success",
            "timestamp" to System.currentTimeMillis()
        )

        paymentRef.set(paymentData)
            .addOnSuccessListener {
                Log.d("Payment", "Payment details saved successfully!")
            }
            .addOnFailureListener { e ->
                Log.e("Payment", "Error saving payment details", e)
            }
    }



    override fun onPaymentError(code: Int, description: String?) {
        Log.e(ContentValues.TAG, "Payment Failed. Code: $code, Description: $description")
        Toast.makeText(this, "Payment Failed. Please try again.", Toast.LENGTH_LONG).show()
    }
    private fun getPaymentId(name: String): String {
        val firstName = name.split(" ").firstOrNull()?.capitalize() ?: "User"
        val uuid = UUID.randomUUID().toString().take(8) // Use first 8 characters for brevity
        return "$firstName-$uuid"
    }


}