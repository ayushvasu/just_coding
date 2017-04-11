package com.glassbeam.scalar.extensibility.didata

import com.glassbeam.extensibility.traits._
import java.io._
import java.nio.file._
import scala.util.parsing.json._
import scala.io.Source
import collection.mutable
import scala.collection.mutable
import scala.util.Try


//import scala.util.parsing.json._
/*class CC[T] {
  def unapply(a:Option[Any]):Option[T] = if (a.isEmpty) {
    None
  } else {
    Some(a.get.asInstanceOf[T])
  }
}

    
   
   
 
    
  
				object M extends CC[Map[String, Any]] {}
				object L extends CC[List[Any]] {}
				object S extends CC[String] {}
				object D extends CC[Double] {}
				object B extends CC[Boolean] {}
   for {
  M(map) <- List(JSON.parseFull(Source.fromFile("""/home/ugan/didata/glassbeam/mcps.json""").mkString))
  L(languages) = map.get("languages")
  language <- languages
  M(lang) = Some(language)
  S(name) = lang.get("name")
  B(active) = lang.get("is_active")
  D(completeness) = lang.get("completeness")
} yield {
  (name, active, completeness)
}*/

class Json extends Parsable {

//val json:Optin[Any] =
def processToFile(p: Path): Path = {
var out_file_name = p.getFileName().toString()
var file_out = p.getParent.toString+"/extensibility."+out_file_name+".log"
var id=""                                         //> id  : String = ""
var geo=""                                        //> geo  : String = ""
var epoch=""                                      //> epoch  : String = ""
var day =""                                       //> day  : String = ""
var hour=""                                       //> hour  : String = ""
var yday=""                                       //> yday  : String = ""
var year=""                                       //> year  : String = ""
var month=""
JSON.globalNumberParser = {in => try { in.toInt } catch { case e => in.toDouble}}

val json:Option[Any] = JSON.parseFull(Source.fromFile(p.toString).mkString)
                                                  //> json  : Option[Any] = Some(List(Map(_id -> Map($oid -> 561b2491d4426b2fb204
                                                  //| 80c8), general -> Map(street_address_1 -> , city -> , zipcode -> , vendor_i
                                                  //| d -> , state -> , home_location -> true, description -> Dimension Data Aust
                                                  //| ralia - Canberra, cancelled_date -> null, entity_state -> NORMAL, country -
                                                  //| > , id -> 8b4f98e6-0fe5-4755-b82b-7e130fcdb340, street_address_2 -> , trust
                                                  //| _level -> 10, referrer_id -> , type -> VENDOR, vendor -> ddushomenetterms),
                                                  //|  geo -> AC, report_period -> Map(epoch -> 1444619415, year -> 2015, hour ->
                                                  //|  3, yday -> 285, day -> 12, month -> 10)), Map(_id -> Map($oid -> 561b2491d
                                                  //| 4426b2fb20480c9), general -> Map(street_address_1 -> , city -> , zipcode ->
                                                  //|  , vendor_id -> , state -> , home_location -> true, description -> Canberra
                                                  //|  Test Vendor, cancelled_date -> null, entity_state -> NORMAL, country -> , 
                                                  //| id -> 9caec6f7-cf65-4324-b607-10421f4b3577, street_address_2 -> , trust_lev
                                                  //| el -> 10, referrer_id -
                                                  //| Output exceeds cutoff limit.

var writer = new PrintWriter(new BufferedWriter((new FileWriter(file_out,true))))
                                                  //> writer  : java.io.PrintWriter = java.io.PrintWriter@24b1d79b
//val json:Option[Any] = JSON.parseFull(jsonString)
val list:List[Map[String,Any]] = json.get.asInstanceOf[List[Map[String,Any]]]
                                                  //> list  : List[Map[String,Any]] = List(Map(_id -> Map($oid -> 561b2491d4426b2
                                                  //| fb20480c8), general -> Map(street_address_1 -> "", city -> "", zipcode -> "
                                                  //| ", vendor_id -> "", state -> "", home_location -> true, description -> Dime
                                                  //| nsion Data Australia - Canberra, cancelled_date -> null, entity_state -> NO
                                                  //| RMAL, country -> "", id -> 8b4f98e6-0fe5-4755-b82b-7e130fcdb340, street_add
                                                  //| ress_2 -> "", trust_level -> 10, referrer_id -> "", type -> VENDOR, vendor 
                                                  //| -> ddushomenetterms), geo -> AC, report_period -> Map(epoch -> 1444619415, 
                                                  //| year -> 2015, hour -> 3, yday -> 285, day -> 12, month -> 10)), Map(_id -> 
                                                  //| Map($oid -> 561b2491d4426b2fb20480c9), general -> Map(street_address_1 -> "
                                                  //| ", city -> "", zipcode -> "", vendor_id -> "", state -> "", home_location -
                                                  //| > true, description -> Canberra Test Vendor, cancelled_date -> null, entity
                                                  //| _state -> NORMAL, country -> "", id -> 9caec6f7-cf65-4324-b607-10421f4b3577
                                                  //| , street_address_2 -> "
                                                  //| Output exceeds cutoff limit.

/*val languages:List[Any] = map.get("_id").get.asInstanceOf[List[Any]]

languages.foreach( langMap => {
val language:Map[String,Any] = langMap.asInstanceOf[Map[String,Any]]
val name:String = language.get("$oid").get.asInstanceOf[String]
val isActive:Boolean = language.get("city").get.asInstanceOf[Boolean]
val completeness:Double = language.get("completeness").get.asInstanceOf[Double]
 
})*/

list.foreach(idmap => {
//val map:Map[String,Any] = json.get.asInstanceOf[Map[String, Any]]


//println(idmap)
		var idtrmp=idmap.get("_id")
		for(idFinal<-idtrmp)
			{
				id=idFinal.asInstanceOf[Map[String,String]].get("$oid").mkString

			}


			geo=idmap.get("geo").mkString
			val t = idmap.get("report_period")//.asInstanceOf[Map[String,Any]]
			for(some<-t)
					{
						epoch=some.asInstanceOf[Map[String,Any]].get("epoch").mkString
						day=some.asInstanceOf[Map[String,Any]].get("day").mkString
						hour=some.asInstanceOf[Map[String,Any]].get("hour").mkString
						yday=some.asInstanceOf[Map[String,Any]].get("yday").mkString
						year=some.asInstanceOf[Map[String,Any]].get("year").mkString
						month=some.asInstanceOf[Map[String,Any]].get("month").mkString


//println(epoch)

					}
			writer.write("Start oid="+id+"==GEO="+geo+"==EPOCH="+epoch+"\n")
			if(idmap.contains("ganymede_doc_version"))
			writer.write("ganymede_doc_version="+ idmap.get("ganymede_doc_version").mkString+"\n")
			writer.write("report_period day="+day+"\n")
			writer.write("report_period hour="+hour+"\n")
			writer.write("report_period yday="+yday+"\n")
			writer.write("report_period year="+year+"\n")
			writer.write("report_period epoch="+epoch+"\n")
			writer.write("report_period month="+month+"\n")


			val sites=idmap.get("sites")
//println(sites.toString)
			for(sitepart<-sites)
				{
						
					var listSite=sitepart.asInstanceOf[List[Map[String,String]]]
					for(lstSite<-listSite)
							{ writer.write("Sub Start sites"+"\n")
								var lstMap=lstSite.asInstanceOf[Map[String,String]]
								
								for((key,city)<-lstMap)
										{
											key match
												{
													case "city" => writer.write("sites city="+city+"\n")
													case "country" => writer.write("sites country=" +city+"\n" )
													case "display_name" => writer.write("sites display_name=" +city+"\n")
													case "mcp_id" => writer.write("sites mcp_id="+city +"\n")
													case "site_id" => writer.write("sites site_id="+city+"\n")
													case "site_name" => writer.write("sites site_name="+city+"\n" )
													case "state" => writer.write("sites state="+city+"\n")
													case "type" => writer.write("sites type="+ city+"\n")
													case _ => //println("Nothing matched")
												}
										}
								}

					}
//val epoch=t.get("epoch").toString

//println(t)
   	if(idmap.contains("baas"))
   		{
  			 writer.write("baas="+idmap.get("baas").mkString+"\n")
   		}
   		
   	var general=idmap.get("general")
   	if(idmap.contains("general"))
   		{
   			var general=idmap.get("general")
   			for(gen<-general)
   				{
   				  var genMap=gen.asInstanceOf[Map[String,Any]]
   				  //println(genMap.toString)
   		       for((key,value) <- genMap)
   		       {
   		        
   		         key match
   		         {
   		           case "created" => var date = value.asInstanceOf[Map[String,String]]
   		           									 //println(value.mkString)
   		           									 writer.write("general created $date="+date.get("$date").mkString+"\n")
   		           case "description" => writer.write("general description="+value+"\n")
   		           case "device_type" => writer.write("general device_type=" +value +"\n")
   		           case "location" => writer.write("general location=" +value +"\n")
   		           case "machine_name" => writer.write("general machine_name="+value+"\n")
   		           case "name" => 						writer.write("general name="+value+"\n")
   		           case "org_id" => writer.write("general org_id="+value+"\n")
   		           case "parent_id" => writer.write("general parent_id="+value+"\n")
   		           case "server_id" => writer.write("general server_id="+value+"\n")
   		           //case "state" => writer.write("general state="+value+"\n")
   		           case "virtual_machine_id" => writer.write("general virtual_machine_id="+value+"\n")
   		           case "cancelled_date" => writer.write("general cancelled_date="+value+"\n")
   		           case "city" => writer.write("general city="+value+"\n")
   		           case "country" => writer.write("genera country="+value+"\n")
   		           //case "description" => writer.write("general description="+value+"\n")
   		           case "entity_state" => writer.write("general entity_state="+value+"\n")
   		           case "home_location" => writer.write("general home_location="+value+"\n")
   		           case "id" => writer.write("general id="+value+"\n")
   		           case "referrer_id" => writer.write("general referrer_id="+value+"\n")
   		           case "state" => writer.write("general state="+value+"\n")
   		           case "street_address_1" =>  writer.write("general street_address_1="+value+"\n")
   		           case "street_address_2" => writer.write("general street_address_2="+value+"\n")
   		           case "trust_level" => writer.write("general trust_level="+value+"\n")
   		           case "type" => writer.write("general type="+value+"\n")
   		           case "vendor"=>writer.write("general vendor="+value+"\n")
   		           case "vendor_id" => writer.write("general vendor_id="+value+"\n")
   		           case "zipcode" => writer.write("general zipcode="+value+"\n")
   		           case "image_id" => writer.write("general image_id="+value+"\n")
   		           case "parent" => writer.write("general parent="+value+"\n")
   		           case "source" => writer.write("general source="+value+"\n")
   		           
   		           
   		           										 
   		           case _ => //println("Nothing Matched")
   		         }
   		       }
   				  
   				}
   	if(idmap.contains("hardware"))
   	{
   	 for(hdr<-idmap.get("hardware"))
   	 
   	 {
   		var hardware=hdr.asInstanceOf[Map[String,Any]]
		var cpu=hardware.get("cpu")
		for(cpuTemp<-cpu)
		{
		var cpuCore=cpuTemp.asInstanceOf[Map[String,String]]
		writer.write("hardware cpu cores="+cpuCore.get("cores").mkString+"\n")
		writer.write("hardware cpu sockets="+cpuCore.get("sockets").mkString+"\n")
		}
		writer.write("hardware os_display_name="+hardware.get("os_display_name").mkString+"\n")
		writer.write("hardware os_family="+hardware.get("os_family").mkString+"\n")
		var ram=hardware.get("ram")
		for(ramTemp<-ram)
		{
			var ramDetails=ramTemp.asInstanceOf[Map[String,String]]
			writer.write("hardware ram size="+ramDetails.get("size").mkString+"\n")
			writer.write("hardware ram unit="+ramDetails.get("unit").mkString+"\n")
		
		}

   		for((key,value)<-hardware)
   			{
   			  writer.write("")
   				key match
   				{
   				 case "disk" => var diskList=value.asInstanceOf[List[Map[String,Any]]]
   				 								//	println(value.toString)
													for(lstTemp<-diskList)
													{writer.write("Sub Start Disk"+"\n")
														for((key,value)<-lstTemp)
														{
															key match
															{
																case "capacity" => var size=value.asInstanceOf[Map[String,Any]]
																										writer.write("hardware disk capacity size="+size.get("size").mkString+"\n")
																										writer.write("hardware disk capacity unit="+size.get("unit").mkString+"\n")
																case "disk_id" => writer.write("hardware disk disk_id="+value.toString.mkString+"\n")
																case "disk_type"=> writer.write("hardware disk disk_type="+value.toString.mkString+"\n")
																case "scsi_id" => writer.write("hardware disk scsi_id="+value.toString.mkString+"\n")
																case "speed" => writer.write("hardware disk speed= "+value.toString.mkString+"\n")
																case "state" => writer.write("hardware disk state="+value.toString.mkString+"\n")
																case _ =>// println("Notj=hing")
															}
														}
													
													}
					 case "nic" => var nic=value.asInstanceOf[List[Map[String,Any]]]
					 							 for(nicList<-nic)
					 							 {writer.write("Sub Start nic"+"\n")
					 							 		for((key,value)<-nicList)
					 							 		{
					 							 		 
					 							 			key match
					 							 			{
					 							 				case "attachment" => writer.write("hardware nic attachment="+value.toString.mkString+"\n")
					 							 				case "ipv4_address" => writer.write("hardware nic ipv4_address="+value.toString.mkString+"\n")
					 							 				case "ipv6_address" => writer.write("hardware nic ipv6_address="+value+"\n")
					 							 				case "nic_id" => writer.write("hardware nic nic_id="+value.toString.mkString+"\n")
					 							 				case "primary_nic" => writer.write("hardware nic primary_nic="+value.toString.mkString+"\n")
					 							 				case "state" => writer.write("hardware nic state="+value.toString.mkString+"\n")
					 							 				case "virtual_nic_id" =>
					 							 																 writer.write("hardware nic virtual_nic_id="+value+"\n") ;
					 							 																	
					 							 				case _ => //println("Nothing ")
					 							 			
					 							 			}
					 							 		
					 							 		}
					 							 }
					 case "software_label" => var softLabel=value.asInstanceOf[List[Map[String,Any]]]
					 															for(softTemp<-softLabel)
					 															{writer.write("Sub Start software_label"+"\n")
					 															  for((key,value)<-softTemp)
					 															  {
					 															  
					 															    key match
					 															    {
					 															    
					 															  			case "label_id" => writer.write("hardware software_label label_id="+value.toString.mkString+"\n")
					 															  			case "display_name" => writer.write("hardware software_label display_name="+value.toString.mkString+"\n")
					 															  			case "description" => writer.write("hardware software_label description="+value.toString.mkString+"\n")
					 															  			case  _ =>
					 															  	}
					 															  }
					 															}
					 case "status" => var status =value.asInstanceOf[Map[String,Any]]
					 									writer.write("status started="+Try(status.get("started").mkString)+"\n")
   				 case _ => //println(value.toString)
   				}
   			}
   	}
   		}	//println(crtDate.mkString)
      }
   	
   	
   	     
   	 
   	
   	
   	    
   	
   	
   	
   	
   	
   	
   	
   	   
   	
   	
   	
   	 
   	
   	
   	
   	
   	
   	 
   	 
   	
   	
   	
   	
   	
   	
   	
   

	})
	//println("I am working")
writer.close
return FileSystems.getDefault().getPath(file_out)


   }
	def processFileToContext(p: Path): String ={
		return "success"
	}
  	def processBundleToContext(p: Path): String ={
		return "success"
	}
	def findFilesOfPattern(infiles: List[Path], outfile: Path): Unit={
	}

  }
   
   
  
