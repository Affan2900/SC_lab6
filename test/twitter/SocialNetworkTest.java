package twitter;
import org.junit.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.*;

public class SocialNetworkTest {
	
	private Tweet createTweet(long id, String author, String text) {
        return new Tweet(id, author, text, Instant.now());
    }

    // Test Case 1: Empty List of Tweets
    @Test
    public void testEmptyListOfTweets() {
        List<Tweet> tweets = new ArrayList<>();
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(graph.isEmpty());
    }

    // Test Case 2: Tweets Without Mentions
    @Test
    public void testTweetsWithoutMentions() {
        List<Tweet> tweets = Arrays.asList(
            createTweet(1, "user1", "Hello World!"),
            createTweet(2, "user2", "No mentions here.")
        );
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertEquals(2, graph.size());
        assertTrue(graph.get("user1").isEmpty());
        assertTrue(graph.get("user2").isEmpty());
    }

    // Test Case 3: Single Mention
    @Test
    public void testSingleMention() {
        List<Tweet> tweets = Collections.singletonList(createTweet(1, "user1", "Hello @user2!"));
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertEquals(2, graph.size());
        assertTrue(graph.get("user1").contains("user2"));
    }


    // Test Case 4: Multiple Mentions
    @Test
    public void testMultipleMentions() {
        List<Tweet> tweets = Collections.singletonList(createTweet(1, "user1", "Hi @user2 and @user3!"));
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertEquals(3, graph.size());
        assertTrue(graph.get("user1").contains("user2"));
        assertTrue(graph.get("user1").contains("user3"));
    }

    // Test Case 5: Multiple Tweets from One User
    @Test
    public void testMultipleTweetsFromOneUser() {
        List<Tweet> tweets = Arrays.asList(
            createTweet(1, "user1", "Hey @user2!"),
            createTweet(2, "user1", "Hello again @user3!")
        );
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertEquals(3, graph.size());
        assertTrue(graph.get("user1").contains("user2"));
        assertTrue(graph.get("user1").contains("user3"));
    }

    // Test Case 6: Empty Graph for influencers
    @Test
    public void testEmptyGraphForInfluencers() {
        Map<String, Set<String>> graph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(graph);
        assertTrue(influencers.isEmpty());
    }

    // Test Case 7: Single User Without Followers
    @Test
    public void testSingleUserWithoutFollowers() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("user1", new HashSet<>());
        List<String> influencers = SocialNetwork.influencers(graph);
        assertTrue(influencers.isEmpty());
    }



    // Test Case 10: Tied Influence
    @Test
    public void testTiedInfluence() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("user1", new HashSet<>(Arrays.asList("user2", "user3")));
        graph.put("user2", new HashSet<>(Arrays.asList("user3")));
        graph.put("user3", new HashSet<>(Arrays.asList("user1")));
        
        List<String> influencers = SocialNetwork.influencers(graph);
        assertEquals(3, influencers.size());
        assertTrue(influencers.contains("user1"));
        assertTrue(influencers.contains("user2"));
        assertTrue(influencers.contains("user3"));
        // In case of a tie, order may vary, but all three should be present.
    }
}
