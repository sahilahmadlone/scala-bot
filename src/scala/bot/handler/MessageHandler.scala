package scala.bot.handler

import scala.bot.learn.Learner
import scala.util.Random

trait MessageHandler extends Learner {
  def provideReply(replies: List[String]): String =
    Random.shuffle(replies).head

  def handle(brain: Templates, msg: String): String = {
    val possibleReplies = brain.filterKeys(k => k._2 == msg)
    val response = provideResponse(possibleReplies, msg)

    BotLog.botLog = BotLog.botLog ++ List(response)
    HumanLog.humanLog = HumanLog.humanLog ++ List(msg)

    response
  }

  def provideResponse(possibleReplies: Templates, message: String): String = {
    val lastBotMessage = BotLog.botLog.last
    val responseWithHistory = (Some(lastBotMessage), message)

    possibleReplies.get(responseWithHistory) match {
      case None          => possibleReplies.get((None, message)) match {
        case None          => "I am sorry, I do not own information of this kind"
        case Some(replies) => provideReply(replies)
      }
      case Some(replies) => provideReply(replies)
    }
  }
}