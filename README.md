caption-parser
=============

A scala parser for the [.srt](https://wiki.videolan.org/SubRip/) and [.vtt](https://www.w3.org/TR/webvtt1/) subtitle/caption file formats.

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

This parser is able to read not only the .srt/.vtt files that respect the syntax, but also the real-life .srt/.vtt files found on the web which sometimes have a _very_ loose interpretation of it.
It is successfully tested against a set of 440+ .srt files coming from various sources and for various medias. Hopefully, if VLC can run a .srt file, this parser should be able to parse it.

The only constraint is that the given file has to be UTF-8 encoded. Auto-detection of the encoding may come in the future (or not).

# TODOs

**VTT**

* Support for `NOTE` comments
* Support for parsing (in a meaningful way) WebVTT header information like:
  * `REGION`
  * `STYLE`

