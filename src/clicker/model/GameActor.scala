package clicker.model

import akka.actor.{Actor, ActorRef}
import clicker._
import play.api.libs.json.{JsNumber, JsObject, JsString, JsValue, Json}

class GameActor(username: String, database: ActorRef) extends Actor {
  var gold: Double = 0
  val clicker: Clicker = new Clicker
  val equipment: Equipment = new Equipment
  val goldPerSecond: GoldPerSecond = new GoldPerSecond
  val jsonHandler: JsonHandler = new JsonHandler
  var lastUpdate: Long = System.nanoTime()
  database ! StartedGame(username)

  override def receive: Receive = {
    case ClickGold =>
      gold += clicker.onClick(equipment)
    case BuyEquipment(equipmentId) =>
      equipment.buyEquip(gold, equipmentId)
      gold += equipment.goldChange
      equipment.goldChange = 0
    case Update =>
      gold += goldPerSecond.income(lastUpdate, System.nanoTime(), equipment)
      lastUpdate = System.nanoTime()
      sender() ! GameState(jsonHandler.jsonFormatting(username, gold, lastUpdate, equipment))
    case Save =>
      database ! SaveGame(username, jsonHandler.jsonFormatting(username, gold, lastUpdate, equipment))
    case GameState(gameState) =>
      val parsedGameState: JsValue = Json.parse(gameState)
      jsonHandler.restoreFromState(parsedGameState, equipment)
      gold = (parsedGameState \ "gold").as[Double]
      gold += goldPerSecond.income((parsedGameState \ "lastUpdateTime").as[Long], System.nanoTime(), equipment)
    case ChangeConfig(configuration) =>
      val parsedConfig: JsValue = Json.parse(configuration)
      println(parsedConfig)
      clicker.shovelClick = (parsedConfig \ "equipment" \  0 \ "incomePerClick").as[Int]
      clicker.excavatorClick = (parsedConfig \ "equipment" \  1 \"incomePerClick").as[Int]
      clicker.mineClick = (parsedConfig \ "equipment" \  2 \"incomePerClick").as[Int]
      equipment.shovelCost = (parsedConfig \ "equipment" \  0 \ "initialCost").as[Double]
      equipment.excavatorCost = (parsedConfig \ "equipment" \  1 \ "initialCost").as[Double]
      equipment.mineCost = (parsedConfig \ "equipment" \  2 \ "initialCost").as[Double]
      equipment.shovelCostChange = (parsedConfig \ "equipment" \  0 \ "priceExponent").as[Double]
      equipment.excavatorCostChange = (parsedConfig \ "equipment" \  1 \ "priceExponent").as[Double]
      equipment.mineCostChange = (parsedConfig \ "equipment" \  2 \ "priceExponent").as[Double]
      equipment.shovelPassive = (parsedConfig \ "equipment" \  0 \ "incomePerSecond").as[Double]
      equipment.excavatorPassive = (parsedConfig \ "equipment" \  1 \ "incomePerSecond").as[Double]
      equipment.minePassive = (parsedConfig \ "equipment" \  2 \ "incomePerSecond").as[Double]
      equipment.shovelID = (parsedConfig \ "equipment" \  0 \ "id").as[String]
      equipment.excavatorID = (parsedConfig \ "equipment" \  1 \ "id").as[String]
      equipment.mineID = (parsedConfig \ "equipment" \  2 \ "id").as[String]
      equipment.shovelName = (parsedConfig \ "equipment" \  0 \ "name").as[String]
      equipment.excavatorName = (parsedConfig \ "equipment" \  1 \ "name").as[String]
      equipment.mineName = (parsedConfig \ "equipment" \  2 \ "name").as[String]
  }
}
