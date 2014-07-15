Your Twitter Network: OSCON 2014 Puzzle
=======================================

Introduction
------------

In December 2012 [Twitter introduced the ability](https://blog.twitter.com/2012/your-twitter-archive)
for any user to download their personal Twitter archive. If you're logged into
Twitter and go to your account settings, you'll see a button labeled
"Request your archive". If you click this button, you'll receive an email with a
link that will allow you to download your tweets (serialized as JSON) together
with an interface that will allow you to view them locally.

This interface lets you search your tweets or go back to see what you were
talking about on Twitter at some particular point in time. It's a fun way to
interact with your archive, but in this puzzle you'll be asked to dig a little
deeper into the data.

The data
--------

If you've been using Twitter for a while, your archive will contain a lot of
interesting information—including a very detailed picture of who you've
interacted with on Twitter, and how. For example, you can create a
[co-occurrence network](http://en.wikipedia.org/wiki/Co-occurrence_networks) by
looking at which Twitter accounts you mention together in a single tweet. Take
[this recent tweet](https://twitter.com/finagle/status/476035983335362560) by
[@finagle](https://twitter.com/finagle):

> What do [@twitter](https://twitter.com/twitter), [@tumblr](https://twitter.com/tumblr),
> [@Pinterest](https://twitter.com/Pinterest), and [@nest](https://twitter.com/nest)
> have in common? Check out our new list of Finagle adopters to see:
> [https://github.com/twitter/finagl…](https://github.com/twitter/finagle/blob/master/ADOPTERS.md)

The fact that these four companies (Twitter, Tumblr, Pinterest, and Nest) are
mentioned in the same tweet (i.e., they _co-occur_ in a document) suggests that
there is some kind of link between them. If you count all of these links in your
Twitter archive, you can create a weighted network that includes every Twitter
user you've ever engaged with, where the weight of the link between two users is
the number of times that they've been mentioned together in a tweet.

The problem
-----------

The goal is to write a Scala program that will generate such a co-occurrence
network (and answer a couple of questions about it!) when pointed at a Twitter
archive. If you don't have a Twitter account, don't worry—you can use either the
the [@finagle](https://twitter.com/finagle) archive (which is included in this
repository in the `sample` directory) or the [@TwitterOSS](https://twitter.com/TwitterOSS)
archive, which is [also available on GitHub](https://github.com/twitter/twitteross-archive).
All submissions will be evaluated against the [@TwitterOSS](https://twitter.com/TwitterOSS)
account, but we encourage you to develop against your own.

See the `sample` directory for an example of correctly formatted output.

### Part 1: Degree

[Degree](http://en.wikipedia.org/wiki/Degree_%28graph_theory%29) is one of the
most important metrics in a social network: the degree of a node is the number
of nodes it's linked to. The first five lines of output of your program should
be the numeric identifier and degree value for the five users in your network
with the highest degrees (with IDs and degrees separated by whitespace).
For my account ([@travisbrown](https://twitter.com/travisbrown)), for example,
that currently looks like this:

```
6510972 118
29444566 67
181862144 49
14086852 48
45966787 29

```

The five lines should be followed by a single blank line.

### Part 2: Node strength

Node strength is a closely related metric in a
[weighted network](http://en.wikipedia.org/wiki/Weighted_network)—it's the sum
of the weights for all of a node's links. For example, if in my archive
[@finagle](https://twitter.com/finagle) is only mentioned in two tweets with
[@TwitterOSS](https://twitter.com/TwitterOSS) and in three with
[@finatra](https://twitter.com/finatra), its node strength would be `2 + 3 = 5`.
Your program should output the identifiers and strength values for the five
strongest nodes in your co-occurrence network (again followed by a blank line).

### Part 3 (extra credit): Weighted degree centrality

[Centrality](http://en.wikipedia.org/wiki/Degree_centrality#Degree_centrality)
is a key concept in social network analysis—it measures the relative importance
of a node in the network.
[Opsahl, T., Agneessens, F., and Skvoretz, J. (2010)](http://toreopsahl.com/2010/04/21/article-node-centrality-in-weighted-networks-generalizing-degree-and-shortest-paths/) define a version of _degree
centrality_ for weighted networks. Their metric includes a tuning parameter
`alpha` that determines the relative importance of strength vs. weight. For
extra credit, output the five user IDs with the highest weighted degree
centrality values for `alpha = 0.5`, together with those values. For my account,
again, that looks like this:

```
6510972 173.4646938140439
29444566 127.85929766739687
181862144 85.73214099741124
14086852 66.81317235396025
548301113 44.58699361921591

```

### Part 4 (super extra credit): Network partitions

Like any other network, the weighted co-occurrence network for the Twitter
archive you're looking at is liable to be partitioned—i.e., to include multiple
[connected components](http://en.wikipedia.org/wiki/Connected_component_%28graph_theory%29).
Print out each connected component on its own line as a space-separated list of
IDs.

Details and gotchas
-------------------

* Only include the users in the `entity_mentions` field when you're adding
co-occurence links. This means that you won't be included in your own tweets.
For example, in the [@finagle](https://twitter.com/finagle) tweet above,
we'd increment the links between [@twitter](https://twitter.com/twitter),
[@tumblr](https://twitter.com/tumblr),
[@Pinterest](https://twitter.com/Pinterest), and
[@nest](https://twitter.com/nest), but not [@finagle](https://twitter.com/finagle)
itself.

* Note also that you may still be the most central user in your own network if
you've retweeted a lot of @-replies directed at you (or if you spend a lot of
time talking about yourself in the third person).

* It's possible to mention a user multiple times in a single tweet—in these cases
you should update your counts as if the user had only been mentioned once.

* Because Twitter screen names can change, you should use the numeric user ID to
keep track of users. This identifier
[can be a very large number](https://blog.twitter.com/2013/test-accounts-user-ids-greater-32-bits)—you
shouldn't assume it can fit in a 32-bit integer. Visiting
`https://twitter.com/account/redirect_by_id/123456789` will take you to the
account for the user with ID 123456789.

* In the case of ties, sort by user ID, with smaller values coming first. For
example, if both [@kanyewest](https://twitter.com/kanyewest) (181862144) and
[@TwitterOSS](https://twitter.com/TwitterOSS) (376825877) have a degree of 29 in
your network, Kanye wins.

* The files in the `data/js/tweets/` directory are JavaScript files, not JSON, so
you'll need to clean them up (a very little bit) before parsing them as JSON.

* You may assume that any of the files in the `data/js/tweets` directory will fit
in memory.

Setup and submissions
---------------------

Your program should expect a single command-line argument: the path to the root
of the unzipped archive. For example, we should be able to run it like this
(note that you'll
[need to have SBT installed](http://www.scala-sbt.org/0.13/tutorial/Setup.html)):

``` bash
sbt "run sample/finagle/"
```

The correct output for the sample archive is included in
`sample/finagle-output.txt`.

We're happy to look at programs that aren't built by SBT (or programs written in
languages other than Scala), but we can't promise they'll be evaluated!

Just tweet or DM a link to a [gist](https://gist.github.com/) or GitHub project
to [@TwitterOSS](https://twitter.com/TwitterOSS) before midnight on Tuesday,
July 22. You may assume the `build.sbt` file in this repository, or you can use
your own—just be sure to include it with your code if you do.

Good luck, have fun, and let us know if you have questions!

Licensing
---------

© Copyright 2014 Twitter, Inc. All code is released under the
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
The problem statement and other pieces of documentation are available under the
[Creative Commons Attribution 3.0 Unported License](https://creativecommons.org/licenses/by/3.0/)
(CC BY).
