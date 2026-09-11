# ThirdCross

ThirdCross is a lightweight, client-side crosshair utility for Minecraft. It provides configurable first- and third-person crosshairs, including an Aim mode that follows the point your player is actually targeting.

## Features

- Separate first-person and third-person crosshair settings
- Four crosshair modes: Off, Static, Aim, and Static + Aim
- Aim-point detection for blocks and entities
- Optional distance-based Aim sizing using the player's interaction ranges
- Configurable Aim smoothing with snap protection
- Cross and Circle styles for the centered crosshair in Static + Aim
- Resource-pack-compatible Aim crosshair rendering
- Optional vanilla-style contextual crosshair tinting
- Optional crosshair in back-facing third-person view
- Optional F5 perspective skipping
- Compatibility with Better Third Person, Sable, and Create Aeronautics physics objects

## Crosshair Modes

For both first-person and third-person views, ThirdCross provides four modes:

- **Off** — hides the vanilla crosshair.
- **Static** — shows a centered crosshair.
- **Aim** — shows a crosshair at the point your player is actually targeting.
- **Static + Aim** — shows both the centered and aim-point crosshairs.

The Aim crosshair uses the vanilla Minecraft crosshair texture, allowing resource packs that replace the vanilla crosshair to affect the Aim crosshair as well.

## Installation

ThirdCross is built for Minecraft 1.21.1 with NeoForge 21.1.248.

1. Install NeoForge for Minecraft 1.21.1.
2. Download the matching ThirdCross release.
3. Place the JAR in your client's `mods` folder.

ThirdCross is client-side only and does not need to be installed on a dedicated server.

## Configuration

Open the Minecraft Mods menu, select ThirdCross, and choose **Config**. Settings are also stored in the generated `thirdcross-client.toml` file.

### Crosshair Settings

First-person and third-person crosshair modes can be configured independently.

### Aim Settings

- **Aim Smoothing** — controls how smoothly the Aim crosshair follows the target. Higher settings provide smoother movement but respond more slowly.
- **Distance-Based Aim Size** — scales the Aim crosshair based on target distance and the player's interaction range.

### Static Crosshair Style

In **Static + Aim** mode, the centered crosshair can use either:

- **Cross** — the vanilla crosshair.
- **Circle** — a circular crosshair.

### Crosshair Tint

ThirdCross can apply vanilla-style contextual tinting to single crosshair modes. Static + Aim is always rendered without tinting.

### Third-Person Perspective

- **Crosshair in Back-Facing View** — controls whether the crosshair is shown while using the back-facing third-person camera.
- **Skip Back-Facing View** — skips the back-facing third-person camera when cycling perspectives with F5.

## Compatibility

ThirdCross is designed to coexist with camera and crosshair-related mods, resource packs that replace the vanilla crosshair, reach-changing mods, and Sable-based physics objects.

Compatibility has been tested with Better Third Person, Dynamic Crosshair, Ok Zoomer, Quark reach-around, resource-pack crosshairs, Sable, and Create Aeronautics physics objects.

Sable Companion is bundled in the release JAR; Sable itself remains optional.

## Building from Source

ThirdCross requires JDK 21.

```bash
./gradlew build
```
## License

ThirdCross is available under the [MIT License](LICENSE).
