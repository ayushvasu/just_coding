package com.glassbeam.scalar.extensibility.ibm

import java.text.DecimalFormat
import com.glassbeam.extensibility.traits._
import java.io._
import sys.process._
import java.nio.file._
import java.lang.Object
import com.ximpleware.{VTDGen, VTDNav, AutoPilot}

class IBM_Parse extends Parsable {

  def processToFile(p: Path): Path = {
    println("Xml path is = " + p)
    println("Absolute Xml path is = " + p.getParent)

    val vtdgen = new VTDGen()
    val vtdgenp = new VTDGen()
    val vtdgenv = new VTDGen()
    val autopilot = new AutoPilot()
    val autopilotp = new AutoPilot()
    val autopilotv = new AutoPilot()

    autopilot.selectXPath("/com.ibm.inventory/ResourceSet/Resource/ResourceList[@displayName='Partitions']/Resource/Property[@displayName='LIC Features']/Value")
    autopilotv.selectXPath("/com.ibm.inventory/ResourceSet/Resource/ResourceList[@displayName='Partitions']/Resource/Property[@displayName='LIC VRMF']/Value")
    autopilotp.selectXPath("/com.ibm.inventory/ResourceSet/Resource/ResourceList[@displayName='Partitions']/Resource/@uniqueId")
    var status = ""
    var flag = 0
    if (vtdgen.parseFile(p.toString, false) && vtdgenp.parseFile(p.toString, false) && vtdgenv.parseFile(p.toString, false)) {
      val vtdnav = vtdgen.getNav()
      val vtdnavPartions = vtdgenp.getNav()
      val vtdnavv = vtdgenv.getNav()
      autopilot.bind(vtdnav)
      autopilotv.bind(vtdnavv)
      autopilotp.bind(vtdnavPartions)
      var i = -1
      var k = -1
      var j = -1
      while ( {
        i = autopilot.evalXPath; true
      }  && {
        j = autopilotp.evalXPath; j
      } != -1 && {
        k = autopilotv.evalXPath; k
      } != -1) {

	println("Inside while loop")
	var lic_str = ""
	var bundle_key = ""
	var partions_key = ""
	if(i != -1){
         lic_str = vtdnav.toNormalizedString(vtdnav.getText())
	}
	if(j != -1){
         partions_key = vtdnavPartions.toNormalizedString(vtdnavPartions.getText())
	}
	if(k != -1){
         bundle_key = vtdnavv.toNormalizedString(vtdnavv.getText())
	}
        println("Lic String = " + lic_str + " Partions Key " + partions_key + " Bundle Version " + bundle_key)
        var l = new LicCreateFile
        status = l.creatLicFile(lic_str, partions_key, bundle_key, p.getParent.toString)
        println("Status = " + status)
        flag = 1
      }
    }
    autopilot.resetXPath()
    autopilotp.resetXPath()
    autopilotv.resetXPath()

	return FileSystems.getDefault().getPath(status)

/*    if (flag == 1) {
      return FileSystems.getDefault().getPath(status)
    } else {
      val file_out = p.getParent.toString + "/extensibility.LICFeature_xml.txt"
      //val fw = new FileWriter(file_out, true)
      //val bw = new BufferedWriter(fw)
      //val writer = new PrintWriter(bw)
     // writer.write("LIC FEATURE String is Empty\n")
    //  writer.close()
      //bw.close()
      //fw.close()
      return FileSystems.getDefault().getPath(file_out)
    } */
  }

  def processFileToContext(p: Path): String = {
    return "success"
  }

  def processBundleToContext(p: Path): String = {
    return "success"
  }
  def findFilesOfPattern(infiles: List[Path], outfile: Path): Unit={
	}

}

class LicCreateFile {
  def power(x: Int, n: Int): Long = {
    if (n >= 1) x * power(x, n - 1)
    else 1
  }

  val lic = Map(
    "10" -> "OEL",
    "20" -> "PAV",
    "21" -> "RMZ (XRC)",
    "22" -> "RMC (PPRC)",
    "23" -> "FlashCopy (PTC)",
    "24" -> "FlashCopy SE",
    "25" -> "Metro Mirror",
    "26" -> "Global Mirror",
    "27" -> "Metro/Global Mirror",
    "28" -> "RMZ Resync",
    "30" -> "Hyper-PAV",
    "31" -> "High Performance FICON (zHPF)",
    "32" -> "IBM z/OS Distributed Data Backup",
    "40" -> "IBM Database Protection",
    "50" -> "Data Compression",
    "51" -> "Thin Provisioning",
    "52" -> "Encryption Authorization",
    "53" -> "Automated Data Reloc/IBM Sys SG Easy Tier",
    "54" -> "IBM System I/O Priority Manager",
    "55" -> "Easy Tier Server")
  var lic_remove = Map(
    "10" -> "OEL",
    "20" -> "PAV",
    "21" -> "RMZ (XRC)",
    "22" -> "RMC (PPRC)",
    "23" -> "FlashCopy (PTC)",
    "24" -> "FlashCopy SE",
    "25" -> "Metro Mirror",
    "26" -> "Global Mirror",
    "27" -> "Metro/Global Mirror",
    "28" -> "RMZ Resync",
    "30" -> "Hyper-PAV",
    "31" -> "High Performance FICON (zHPF)",
    "32" -> "IBM z/OS Distributed Data Backup",
    "40" -> "IBM Database Protection",
    "50" -> "Data Compression",
    "51" -> "Thin Provisioning",
    "52" -> "Encryption Authorization",
    "53" -> "Automated Data Reloc/IBM Sys SG Easy Tier",
    "54" -> "IBM System I/O Priority Manager",
    "55" -> "Easy Tier Server")
  val hex = Map(
    "0" -> "0000",
    "1" -> "0001",
    "2" -> "0010",
    "3" -> "0011",
    "4" -> "0100",
    "5" -> "0101",
    "6" -> "0110",
    "7" -> "0111",
    "8" -> "1000",
    "9" -> "1001",
    "A" -> "1010",
    "B" -> "1011",
    "C" -> "1100",
    "D" -> "1101",
    "E" -> "1110",
    "F" -> "1111"
  )

  def creatLicFile(lic_str: String, partition_key: String, index: String, fldXml: String): String = {
    val df = new DecimalFormat("###.##")
    val file_out = fldXml + "/extensibility.LICFeature_" + index + "_xml.txt"
    val fw = new FileWriter(file_out, true)
    val bw = new BufferedWriter(fw)
    val writer = new PrintWriter(bw)
    writer.write("LIC FEATURE Partitions Uniq key " + partition_key + "\n")
	if(!lic_str.isEmpty){
    val licarray = lic_str.split(",")
    for (X <- licarray) {
      var scopevalue = ""
      var LicCap = ""
      var LicUse = "NA"
      var LicMax = "NA"
      val lickey_value = X.split("=")
      val lickey = lickey_value(0)
      val licvalue = lickey_value(1)
      if (lic.contains(lickey)) {
        lic_remove -= lickey
        writer.write(lickey + "\t" + lic(lickey) + "\t")
        val lic_cap_use_max = licvalue.split("/")
        var i = 0;
        for (CUM <- lic_cap_use_max) {
          val hexarray = CUM.toCharArray
          var hexstring = ""
          for (H <- hexarray) {
            //                      println("Key "+H)
            hexstring += hex(H.toString)
          }
          if (i == 0) {
            val scopevalue_one = hexstring.substring(0, 2)
            //      println("Substring = "+scopevalue_one)
            if (scopevalue_one.equals("00")) scopevalue = "Reserved"
            else if (scopevalue_one.equals("01")) scopevalue = "FB"
            else if (scopevalue_one.equals("10")) scopevalue = "CKD"
            else scopevalue = "CF"

            val cmpstr = hexstring.substring(4, 16)
            //println("Compare string "+cmpstr)
            if (cmpstr.equals("111111111111")) {
              LicCap = "Enabled"
              //      println("Enabled")
            } else if (cmpstr.equals("000000000000")) {
              LicCap = "Disabled"
              //      println("Disabled")
            } else {
              var scale = 0
              var si = 4
              val scale_bit = hexstring.substring(2, 6).toCharArray
              for (S <- scale_bit) {
                if (S.toString.equals("1")) {
                  if (si == 4) scale = 8
                  else if (si == 3) scale = scale + 4
                  else if (si == 2) scale = scale + 2
                  else if (si == 1) scale = scale + 1
                }
                si = si - 1
              }
              //println("Scale Value = "+scale);

              var li = 10
              var lic_dec = 0
              val lic_ttot = hexstring.substring(6).toCharArray
              for (S <- lic_ttot) {
                //println("6 to 10 Char "+S)
                if (S.toString.equals("1")) {
                  if (li == 10) lic_dec = 512
                  else if (li == 9) lic_dec = lic_dec + 256
                  else if (li == 8) lic_dec = lic_dec + 128
                  else if (li == 7) lic_dec = lic_dec + 64
                  else if (li == 6) lic_dec = lic_dec + 32
                  else if (li == 5) lic_dec = lic_dec + 16
                  else if (li == 4) lic_dec = lic_dec + 8
                  else if (li == 3) lic_dec = lic_dec + 4
                  else if (li == 2) lic_dec = lic_dec + 2
                  else if (li == 1) lic_dec = lic_dec + 1
                }
                li = li - 1
              }
              //println("Lic final data "+lic_dec)
              val scale_to_p = (lic_dec * power(2, scale * 5)) / power(10, 9)
              LicCap = scale_to_p.toString

              //      println("Check this vale "+scale_to_p)
            }
            i = i + 1
          } else if (i == 1) {
            val cmpstr = hexstring.substring(4, 16)
            //println("Compare string "+cmpstr)
            if (cmpstr.equals("111111111111")) {
              LicUse = "Enabled"
              //      println("Enabled")
            } else if (cmpstr.equals("000000000000")) {
              LicUse = "0"
              //      println("Disabled")
            } else {
              var scale = 0
              var si = 4
              val scale_bit = hexstring.substring(2, 6).toCharArray
              for (S <- scale_bit) {
                if (S.toString.equals("1")) {
                  if (si == 4) scale = 8
                  else if (si == 3) scale = scale + 4
                  else if (si == 2) scale = scale + 2
                  else if (si == 1) scale = scale + 1
                }
                si = si - 1
              }
              //println("Scale Value = "+scale);

              var li = 10
              var lic_dec = 0
              val lic_ttot = hexstring.substring(6).toCharArray
              for (S <- lic_ttot) {
                //println("6 to 10 Char "+S)
                if (S.toString.equals("1")) {
                  if (li == 10) lic_dec = 512
                  else if (li == 9) lic_dec = lic_dec + 256
                  else if (li == 8) lic_dec = lic_dec + 128
                  else if (li == 7) lic_dec = lic_dec + 64
                  else if (li == 6) lic_dec = lic_dec + 32
                  else if (li == 5) lic_dec = lic_dec + 16
                  else if (li == 4) lic_dec = lic_dec + 8
                  else if (li == 3) lic_dec = lic_dec + 4
                  else if (li == 2) lic_dec = lic_dec + 2
                  else if (li == 1) lic_dec = lic_dec + 1
                }
                li = li - 1
              }
              //println("Lic final data "+lic_dec)
              val scale_to_p = (lic_dec * power(2, scale * 5)) / power(10, 9)
              LicUse = scale_to_p.toString
            }
            i = i + 1
          } else if (i == 2) {
            val cmpstr = hexstring.substring(4, 16)
            //println("Compare string "+cmpstr)
            if (cmpstr.equals("111111111111")) {
              LicMax = "Enabled"
              //      println("Enabled")
            } else if (cmpstr.equals("000000000000")) {
              LicMax = "0"
              //      println("Disabled")
            } else {
              var scale = 0
              var si = 4
              val scale_bit = hexstring.substring(2, 6).toCharArray
              for (S <- scale_bit) {
                if (S.toString.equals("1")) {
                  if (si == 4) scale = 8
                  else if (si == 3) scale = scale + 4
                  else if (si == 2) scale = scale + 2
                  else if (si == 1) scale = scale + 1
                }
                si = si - 1
              }
              //println("Scale Value = "+scale);

              var li = 10
              var lic_dec = 0
              val lic_ttot = hexstring.substring(6).toCharArray
              for (S <- lic_ttot) {
                //println("6 to 10 Char "+S)
                if (S.toString.equals("1")) {
                  if (li == 10) lic_dec = 512
                  else if (li == 9) lic_dec = lic_dec + 256
                  else if (li == 8) lic_dec = lic_dec + 128
                  else if (li == 7) lic_dec = lic_dec + 64
                  else if (li == 6) lic_dec = lic_dec + 32
                  else if (li == 5) lic_dec = lic_dec + 16
                  else if (li == 4) lic_dec = lic_dec + 8
                  else if (li == 3) lic_dec = lic_dec + 4
                  else if (li == 2) lic_dec = lic_dec + 2
                  else if (li == 1) lic_dec = lic_dec + 1
                }
                li = li - 1
              }
              //println("Lic final data "+lic_dec)
              val scale_to_p = (lic_dec * power(2, scale * 5)) / power(10, 9)
              LicMax = scale_to_p.toString
            }
            i = i + 1
          }
          //println(CUM+" Hex = "+hexstring)

        }
      }
      var cap = "TRUE"
      var use = "TRUE"
      var max = "TRUE"
      var currflag = "N"
      var maxflag = "N"
      var MaxUse1 = 0.0
      var MaxUse = 0.0
      if (LicCap.equals("") && scopevalue.equals("") && LicUse.equals("") && LicMax.equals("")) {
        println("NA||||")
      }
      else {
        if (LicCap.equals("") || LicCap.equals("Disabled") || LicCap.equals("Enabled") || LicCap.equals("NA") || LicCap.toInt <= 0) {
          cap = "FALSE"
        }
        if (LicUse.equals("") || LicUse.equals("Disabled") || LicUse.equals("Enabled") || LicUse.equals("NA") || LicUse.toInt <= 0) {
          use = "FALSE"
        }
        if (LicMax.equals("") || LicMax.equals("Disabled") || LicMax.equals("Enabled") || LicMax.equals("NA") || LicMax.toInt <= 0) {
          max = "FALSE"
        }
        if (cap.equals("TRUE") && use.equals("TRUE") && LicUse.toInt > LicCap.toInt) {
          currflag = "Y"
        }
        if (max.equals("TRUE") && cap.equals("TRUE")) {
          MaxUse1 = LicMax.toFloat / LicCap.toFloat
          if (MaxUse1 > 0.00) {
            MaxUse = MaxUse1
          }
        }
        if (max.equals("TRUE") && cap.equals("TRUE") && LicMax.toInt > LicCap.toInt) {
          maxflag = "Y"
        }

        writer.write(scopevalue + "-" + LicCap + "-" + LicUse + "-" + LicMax + "|" + currflag + "|" + maxflag + "|" + df.format(MaxUse) + "\n")
      }
    }
    lic_remove.keys.foreach { i => writer.write(i + "\t" + lic_remove(i) + "\tNA||||\n") }
	}
    writer.close()
    bw.close()
    fw.close()
    return file_out
  }


}
