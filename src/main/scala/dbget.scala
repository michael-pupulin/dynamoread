import akka.actor.ActorSystem
import akka.stream.alpakka.dynamodb.scaladsl.DynamoDb
import com.github.matsluni.akkahttpspi.AkkaHttpClient
import com.typesafe.config.ConfigFactory
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model._

import java.util
import java.util._
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}



object dbget {

  private val credentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create("accesskey", "secretkey"))

  // Don't encode credentials in your source code!
  // see https://doc.akka.io/docs/alpakka/current/aws-shared-configuration.html


  def main(args: Array[String]): Unit = {
    // creating an actor system and setting up a dynamodb connection
    val cl = getClass.getClassLoader
    implicit val system: ActorSystem = ActorSystem("system", ConfigFactory.load(cl), cl)
    implicit val client: DynamoDbAsyncClient = DynamoDbAsyncClient
      .builder()
      .region(Region.US_EAST_2)
      .credentialsProvider(credentialsProvider)
      .httpClient(AkkaHttpClient.builder().withActorSystem(system).build())
      .build()

    //gets list of table names as a future
    val listTablesResult: Future[ListTablesResponse] =
      DynamoDb.single(ListTablesRequest.builder().build())

    // prints result once complete
    println(Await.result(listTablesResult,10.seconds))

  // You have to make a java map for the GetItemRequest keys function. Why does a scala map not work?
    // here I am defining the object I want to receive, it is called starwars with primary key 3333
    val jm: util.HashMap[String, AttributeValue]= new HashMap()
    jm.put("3333", AttributeValue.builder().s("starwars").build());

    // getting item from dynamo using above java map, specifying the table it exists in
    val data: Future[GetItemResponse] =
      DynamoDb.single(GetItemRequest.builder().key(jm)
        .tableName("guess").build())

    //val gamedata = Await.result(data,10.seconds)
    println(Await.result(data,10.seconds))

    // closing time
    system.registerOnTermination(client.close())
    system.terminate()




  }
}


