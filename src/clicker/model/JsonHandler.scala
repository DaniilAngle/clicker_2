package clicker.model

import play.api.libs.json.{JsNumber, JsObject, JsString, JsValue, Json}

class JsonHandler {
  def jsonFormatting(username: String, gold: Double, lastUpdate: Long, equipment: Equipment): String ={
    val jsValue: JsValue = JsObject(
      Seq(
        "username" -> JsString(username),
        "gold" -> JsNumber(gold),
        "lastUpdateTime" -> JsNumber(lastUpdate),
        "equipment" -> JsObject(
          Seq(
            "shovel" -> JsObject(
              Seq(
                "id" -> JsString(equipment.shovelID),
                "name" -> JsString(equipment.shovelName),
                "numberOwned" -> JsNumber(equipment.shovel),
                "cost" -> JsNumber(equipment.shovelCost)
              )
            ),
            "excavator" -> JsObject(
              Seq(
                "id" -> JsString(equipment.excavatorID),
                "name" -> JsString(equipment.excavatorName),
                "numberOwned" -> JsNumber(equipment.excavator),
                "cost" -> JsNumber(equipment.excavatorCost)
              )
            ),
            "mine" -> JsObject(
              Seq(
                "id" -> JsString(equipment.mineID),
                "name" -> JsString(equipment.mineName),
                "numberOwned" -> JsNumber(equipment.mine),
                "cost" -> JsNumber(equipment.mineCost)
              )
            )
          )
        )
      )
    )
    Json.stringify(jsValue)
  }

  def restoreFromState(jsValue: JsValue, equipment: Equipment): Unit = {
    equipment.shovel = (jsValue \ "equipment" \ "shovel" \ "numberOwned").as[Int]
    equipment.excavator = (jsValue \ "equipment" \ "excavator" \ "numberOwned").as[Int]
    equipment.mine = (jsValue \ "equipment" \ "mine" \ "numberOwned").as[Int]
    equipment.shovelCost = (jsValue \ "equipment" \ "shovel" \ "cost").as[Double]
    equipment.excavatorCost = (jsValue \ "equipment" \ "excavator" \ "cost").as[Double]
    equipment.mineCost = (jsValue \ "equipment" \ "mine" \ "cost").as[Double]
  }
}
