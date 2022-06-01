package clicker.model

class Clicker() {
  var shovelClick: Int = 1
  var excavatorClick: Int = 5
  var mineClick: Int = 0

  def onClick(equipment: Equipment): Double = {
    var currentGold: Double = 0
    currentGold = 1 + equipment.shovel*shovelClick + equipment.excavator*excavatorClick + equipment.mine*mineClick
    currentGold
  }
}
