caption-parser
=============

A scala parser for the [.srt](https://wiki.videolan.org/SubRip/) and [.vtt](https://www.w3.org/TR/webvtt1/) subtitle/caption file formats.

# Using

Add the following to your build.sbt file (scala `v2.11`, `v2.12`, `v2.13` versions available):

```
libraryDependencies +=  "com.crowdscriber.captions" %% "caption-parser" % "0.1.5"
```

# SRT (SubRip)

A .srt file is of the format:

```
    1
    00:00:48,825 --> 00:00:51,725
    - Sir, she's still closing.
    - We simply cannot outrun her.

    2
    00:00:51,843 --> 00:00:54,011
    We must surrender
    while we still can.

    3
    00:00:54,649 --> 00:00:56,405
    Gun crews... at the ready!
```

This file can be parsed like so:

```scala

val subs = SrtDissector(new File("/path/to/sample.srt"))
// subs is of type SubtitleBlock

```

# VTT (WebVTT)

A WebVTT is a more complex format that allows provisions for such things as comments, styles and regions.  This parser, as it stands, concentrates soley on parsing the cue information.

A WebVTT file is of the format:

```
WEBVTT Some header information
Header line
Another header line...

00:00:00.030 --> 00:00:01.610 optional cue information
I'm the 1st line of subtitle 1
 
I'm the 3rd line of subtitle 1. The 2nd line had a space and that's all

2
00:00:01.610 --> 00:00:02.620 optional cue information

I'm the 2nd line of subtitle 2. The first line was blank and this subtitle also has an optional cue header of '2'

```

This file can be parsed like so:

```scala

val subs = VttDissector(new File("/path/to/sample.vtt"))
// subs is of type SubtitleBlock

```

WebVTT files often have markup tags and timing tags included in the caption text, for example:

```
WEBVTT

00:00:00.030 --> 00:00:01.610 align:start position:0%
 all<00:00:00.240><c> right</c><00:00:00.480><c> we</c><00:00:00.690><c> are</c><c.colorCCCCCC><00:00:00.719><c> here</c><00:00:01.079><c> in</c><00:00:01.199><c> sunny</c><00:00:01.439><c> Los</c></c>

00:00:01.610 --> 00:00:01.620 align:start position:0%
Angeles<00:00:02.159><c> and</c><00:00:02.370><c> we're</c><00:00:02.610><c> gonna</c><00:00:02.700><c> be</c></c><c.colorE5E5E5><00:00:02.820><c> doing</c><00:00:03.000><c> episode</c></c>

```

# filterCaptionText

The `<c.colorXXYYZZ>` tags are used to style parts of the caption. YouTube does this for their auto-captions, coloring
certain words differently from others to indicate how confident they were with their auto-captioning of a particular
word or group of words.

There are also timing tags of the format `<hh:MM:ss.mmm>`, these are used for captions that are displayed "karaoke-style"
in which words are displayed as their are spoken on screen, instead of line by line.

More often than not the meta information embedded in the captions is not desired. For this reason,
[SubtitleUtil](src/main/scala/com/crowdscriber/caption/util/SubtitleUtil.scala) provides a method
named `filterCaptionText` that will strip the meta tags out of the caption.

# WebVTT De-duping

YouTube auto-captions display in a "rollup" type fashion. This means that captions are displayed,
line-by-line from the bottom up. A WebVTT auto-caption file generated from YouTube generated for
the video being displayed below will look something like this:

![sample youtube video](https://i.imgur.com/GHycFXm.gif)


```
00:00:00.030 --> 00:00:01.610 align:start position:0%
 
all<00:00:00.240><c> right</c><00:00:00.480><c> we</c><00:00:00.690><c> are</c><c.colorCCCCCC><00:00:00.719><c> here</c><00:00:01.079><c> in</c><00:00:01.199><c> sunny</c><00:00:01.439><c> Los</c></c>

```

Above, the words _all right we are here in sunny Los_ would appear, one by one, on the second line of the subtitle displayed at the bottom of the screen.

```
00:00:01.610 --> 00:00:01.620 align:start position:0%
all right we are<c.colorCCCCCC> here in sunny Los
 </c>

```

And above, the whole line shifts up to the first line of the caption display, without any second line. YouTube is deciding to color
the phrase _here in sunny Los_ differently. Perhaps it is not as confident in how it predicted what was being said here, remember
these captions have been extracted from a video that is auto-captioned.

```
00:00:01.620 --> 00:00:03.200 align:start position:0%
all right we are<c.colorCCCCCC> here in sunny Los
Angeles<00:00:02.159><c> and</c><00:00:02.370><c> we're</c><00:00:02.610><c> gonna</c><00:00:02.700><c> be</c></c><c.colorE5E5E5><00:00:02.820><c> doing</c><00:00:03.000><c> episode</c></c>
```

Now, we see the second line of caption coming in, word-by-word below the first line.


# De-duping

A lot of times you aren't using a caption display mechanism that supports multi-line rollup captions.
In these situations, you really want to "de-duplicate" the captions by only keeping one line of
captions. [SubtitleUtil](src/main/scala/com/crowdscriber/caption/util/SubtitleUtil.scala) provides 
a `vttToSubtitles` convenience method that lets you control whether or not captions are de-duped.

Using this function like so:

```scala
val subs = SubtitleUtil.vttToSubtitles(file("vtt_files/sample_youtube.vtt"), true)
```

Will take a file that looks like this:

```
WEBVTT

00:00:00.030 --> 00:00:01.610 align:start position:0%
 
all<00:00:00.240><c> right</c><00:00:00.480><c> we</c><00:00:00.690><c> are</c><c.colorCCCCCC><00:00:00.719><c> here</c><00:00:01.079><c> in</c><00:00:01.199><c> sunny</c><00:00:01.439><c> Los</c></c>

00:00:01.610 --> 00:00:01.620 align:start position:0%
all right we are<c.colorCCCCCC> here in sunny Los
 </c>

00:00:01.620 --> 00:00:03.200 align:start position:0%
all right we are<c.colorCCCCCC> here in sunny Los
Angeles<00:00:02.159><c> and</c><00:00:02.370><c> we're</c><00:00:02.610><c> gonna</c><00:00:02.700><c> be</c></c><c.colorE5E5E5><00:00:02.820><c> doing</c><00:00:03.000><c> episode</c></c>

00:00:03.200 --> 00:00:03.210 align:start position:0%
Angeles and we're gonna be<c.colorE5E5E5> doing episode
 </c>
```

and reduce it to content that looks like this:

```scala
List(
  SubtitleBlock(30,1620,
    List(
      "all<00:00:00.240><c> right</c><00:00:00.480><c> we</c><00:00:00.690><c> are</c><c.colorCCCCCC><00:00:00.719><c> here</c><00:00:01.079><c> in</c><00:00:01.199><c> sunny</c><00:00:01.439><c> Los</c></c>"
      )
    ),
  SubtitleBlock(1620,3210,
    List(
      "Angeles<00:00:02.159><c> and</c><00:00:02.370><c> we're</c><00:00:02.610><c> gonna</c><00:00:02.700><c> be</c></c><c.colorE5E5E5><00:00:02.820><c> doing</c><00:00:03.000><c> episode</c></c>"
    )
  ),
//...
```
# Caveats

This parser is able to read not only the .srt/.vtt files that respect the syntax, but also the real-life .srt/.vtt files found on the web which sometimes have a _very_ loose interpretation of it.
It is successfully tested against a set of 440+ .srt files coming from various sources and for various medias. Hopefully, if VLC can run a .srt file, this parser should be able to parse it.

The only constraint is that the given file has to be UTF-8 encoded. Auto-detection of the encoding may come in the future (or not).

# TODOs

**VTT**

* Support for `NOTE` comments
* Support for parsing (in a meaningful way) WebVTT header information like:
  * `REGION`
  * `STYLE`

