package com.harshita.pdfprintwithwifiprinter

import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.harshita.pdfprintwithwifiprinter.databinding.ActivityMainBinding
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.border.Border
import com.itextpdf.layout.element.*
import com.itextpdf.layout.property.TabAlignment
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val file_name : String = "text_pdf.pdf"
    private val currentDateandTime: String? = null
    private var todayDate: String? = null
    private var currentTime: String? = null
    var isFromPrinting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Dexter.withContext(this)
            .withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object:PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                binding.btnCPDF.setOnClickListener{
                    File(Comman.getAppPath(this@MainActivity)).mkdirs()
                    createPDFFile(Comman.getAppPath(this@MainActivity)+file_name)
                }
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {

                }

            })
            .check()


        /*---- FORMAT TODAY DATE ----*/
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = Date()
        todayDate = formatter.format(date)



        /*------- CURRENT TIME -----------*/
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.US)
        val time = Calendar.getInstance().time
        currentTime = timeFormatter.format(time)
    }


    fun getCell(textLeft: String?, alignment: TextAlignment?): Cell? {
        val cell = Cell().add(Paragraph(textLeft))
        cell.setPadding(0f)
        cell.setTextAlignment(alignment)
        cell.setBorder(Border.NO_BORDER)
        return cell
    }

    private fun createPDFFile(path: String) {
        if (File(path).exists())
            File(path).delete()
        try {
            val PdfWriter = PdfWriter(path)

            //Initialize PDF document
            val pdf = PdfDocument(PdfWriter)

            // Initialize document
            val document = Document(pdf, PageSize.A4)
            val tableu = Table(2)
            tableu.addCell(getCell(" Date & Time : $currentDateandTime", TextAlignment.LEFT))

                tableu.addCell(getCell(" Dated $todayDate", TextAlignment.RIGHT))
                tableu.addCell(getCell(" Ref. No. : " + "", TextAlignment.LEFT))

            document.add(tableu)
            val tablex = Table(1)
            tablex.addCell(getCell("\n Tax Invoice \n", TextAlignment.CENTER))
            document.add(tablex)
            val tables = Table(2)
            tables.addCell(getCell(" No. of boxes: 10", TextAlignment.CENTER))
            tables.addCell(getCell(" Address : xyz  " , TextAlignment.LEFT))
            tables.addCell(getCell(" Builty no. : ", TextAlignment.CENTER))
            tables.addCell(getCell("  ", TextAlignment.LEFT))
            tables.addCell(getCell(" Dispatch date: 14/06/2022", TextAlignment.CENTER))
            tables.addCell(getCell(" ", TextAlignment.LEFT))
            tables.addCell(getCell(" Transport: Truck", TextAlignment.CENTER))
            tables.addCell(getCell(" GSTIN/UIN : 123456789  ", TextAlignment.LEFT))
            tables.addCell(getCell(" Destination: India", TextAlignment.CENTER))
            tables.addCell(getCell(" State Name : Maharashtra  ", TextAlignment.LEFT))
            document.add(tables)
            val table2 = Table(
                UnitValue.createPercentArray(
                    floatArrayOf(
                        1f,
                        4f,
                        3f,
                        3f,
                        3f,
                        1f,
                        3f
                    )
                )
            ).useAllAvailableWidth()

            // table2 ...01
            table2.addCell(Cell().add(Paragraph("Sr No. ")))
            table2.addCell(Cell().add(Paragraph("Description of Goods")))
            table2.addCell(Cell().add(Paragraph("HSN/SAC")))
            table2.addCell(Cell().add(Paragraph("Quantity ")))
            table2.addCell(Cell().add(Paragraph("Rate ")))
            table2.addCell(Cell().add(Paragraph("Per ")))
            table2.addCell(Cell().add(Paragraph("Amount ")))
            var totalAmount = 0.0
            var totalQty = 0.0
            var index = 0

            val dt = String.format("%.2f", totalAmount)
            table2.addCell(Cell().add(Paragraph("1")))
            table2.addCell(Cell().add(Paragraph("Total")))
            table2.addCell(Cell().add(Paragraph("5265")))
            table2.addCell(Cell().add(Paragraph(totalQty.toString())))
            table2.addCell(Cell().add(Paragraph("50")))
            table2.addCell(Cell().add(Paragraph("2")))
            table2.addCell(Cell().add(Paragraph(java.lang.Double.valueOf(dt).toString())))

            //table2 ...5
            document.add(table2)
            //Middle text
            val pg = Paragraph("Amount Chargeable (in words) \n")
            pg.add(NumberToWord.convert(totalAmount.toLong()).toString() + " only.")
            pg.add(Tab())
            pg.addTabStops(TabStop(1000F, TabAlignment.RIGHT))
            pg.add("E. & O.E")
            document.add(pg)


            val p = Paragraph("")
            p.add("\n")
            p.add("\n Declaration \n")
            p.add("We declare that this invoice shows the actual \n price of the goods described and that all particulars \n are true and correct")
            p.add("\n")
            p.add(Tab())
            p.addTabStops(TabStop(1000F, TabAlignment.RIGHT))
            p.add("For Harshita Bambure \n")
            document.add(p)
            val tableb = Table(1)
            tableb.addCell(getCell("\n Authorised Signnatory", TextAlignment.RIGHT))
            //  tableb.addCell(getCell("This is computer generated Invoice",TextAlignment.CENTER));
            document.add(tableb)
            val tablem = Table(1)
            tablem.addCell(getCell("\n This is computer generated Invoice", TextAlignment.CENTER))
            document.add(tablem)
            val tablelow = Table(2)
            tablelow.addCell(getCell("\n Printed by: A", TextAlignment.LEFT))
            document.add(tablelow)
            document.close()
            Toast.makeText(this, "Sucess", Toast.LENGTH_SHORT).show()
            printPDF()
            isFromPrinting = true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } /* catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    private fun printPDF() {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager

        try {
            val printAdapter = PdfDocumentAdapter(this,Comman.getAppPath(this)+file_name)
            printManager.print("Documents",printAdapter,PrintAttributes.Builder().build())
        }catch (e:Exception){
            Log.e("Harshita",""+e.message)
        }

    }
}


