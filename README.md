# ThirdCross

ThirdCross is a lightweight, client-side crosshair utility for Minecraft. It provides configurable first- and third-person crosshairs, including an Aim mode that follows the point your player is actually targeting.

## Features

- Separate first-person and third-person crosshair settings
- Four crosshair modes: Off, Static, Aim, and Static + Aim
- Aim-point detection for blocks and entities
- Optional distance-based Aim sizing using the player's interaction ranges
- Configurable Aim smoothing with snap protection
- Static Cross and Circle styles
- Resource-pack-compatible Aim crosshair rendering
- Optional vanilla-style crosshair tinting
- Back-facing third-person crosshair and F5 skip options
- Compatibility with Better Third Person, Sable, and Create Aeronautics physics objects

## Installation

ThirdCross is built for Minecraft 1.21.1 with NeoForge 21.1.248.

1. Install NeoForge for Minecraft 1.21.1.
2. Download the matching ThirdCross release.
3. Place the JAR in your client's `mods` folder.

ThirdCross is client-side only and does not need to be installed on a dedicated server.

## Configuration

Open the Minecraft Mods menu, select ThirdCross, and choose **Config**. Settings are also stored in the generated `thirdcross-client.toml` file.

For both first-person and third-person views, select one of these modes:

- **Off** — hides the crosshair.
- **Static** — shows a centered crosshair.
- **Aim** — shows a crosshair at the player's actual aim point.
- **Static + Aim** — shows both the centered and aim-point crosshairs.

## Compatibility

ThirdCross is designed to coexist with camera mods, reach-changing mods, resource packs that replace the vanilla crosshair, and Sable-based physics objects. Sable Companion is bundled in the release JAR; Sable itself remains optional.

## Building from source

ThirdCross requires JDK 21.

```bash
./gradlew build
```

The release JAR is written to `build/libs/`.

## License

ThirdCross is available under the [MIT License](LICENSE).
