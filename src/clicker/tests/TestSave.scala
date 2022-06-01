package clicker.tests

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestActor.RealMessage
import akka.testkit.{ImplicitSender, TestKit}
import clicker.{BuyEquipment, ClickGold, GameState, Save, SaveGame, StartedGame}
import clicker.database.{Database, DatabaseActor, TestDatabase}
import clicker.model.GameActor
import org.scalatest._
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.duration._

class TestSave extends TestKit(ActorSystem("TestSave"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }


  "A Clicker Game" must {
    "save and load properly" in {

      val database = system.actorOf(Props(classOf[DatabaseActor], "test"))
      val gameActor = system.actorOf(Props(classOf[GameActor], "saveLoad", database))

      for (i <- 1 to 201 ) {
        gameActor ! ClickGold
      }
      gameActor ! BuyEquipment("excavator")
      gameActor ! Save
      Thread.sleep(1000)
      database ! StartedGame("saveLoad")
      var gs: GameState = expectMsgType[GameState]
      var gameStateJSON: String = gs.gameState
      gameActor ! GameState(gameStateJSON)
      gameActor ! Save
      Thread.sleep(1000)
      database ! StartedGame("saveLoad")
      gs = expectMsgType[GameState]
      gameStateJSON = gs.gameState
      val gameState: JsValue = Json.parse(gameStateJSON)
      println(gameState)
      val gold: Double = (gameState \ "gold").as[Double]
      assert(gold >= 10)
    }
  }


}
