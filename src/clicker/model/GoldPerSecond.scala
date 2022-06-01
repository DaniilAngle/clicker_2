package clicker.model

class GoldPerSecond {
  def income(lastUpdate: Long, currentTime: Long, equipment: Equipment): Double = {
    val timeDif: Long = currentTime - lastUpdate
    val timeToDouble: Double = timeDif.toDouble / 1000000000
    val incomeEarned: Double = timeToDouble * equipment.excavator * equipment.excavatorPassive +
      timeToDouble * equipment.mine*equipment.minePassive + timeToDouble * equipment.shovel * equipment.shovelPassive
    incomeEarned
  }
}
