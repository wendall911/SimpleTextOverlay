# SimpleTextOverlay — Project Context

## What This Is
A HUD overlay mod displaying configurable information on screen. Intentionally simple —
older versions (1.15.x and below) were feature-heavy and overly complex; the mod was
rebuilt from scratch at 1.18.2 to be minimal and focused.

Overlay options: Time, Light Level, Foot Level, Biome, Season, and location/spawn/death
indicators.

License: MIT

## Project Structure
Multi-loader: `Common/` + `NeoForge/` (+ `Forge/` on older branches) + `Fabric/`

## Branch Convention
| Branch | Modloaders        |
|--------|-------------------|
| 1.18.2 | Forge + Fabric    |
| 1.20.1 | Forge + Fabric    |
| 1.21.1 | NeoForge + Fabric |
| 26.1   | NeoForge + Fabric |

Maintained: 1.20.1, 1.21.1, 26.1

## Dependencies
- WhiteNoise (jarJar/include)
- HomeostaticSeasons (implementation — primary season integration)
- Serene Seasons (optional integration)
- Ecliptic Seasons (optional integration)
- Fabric Seasons (optional integration — Fabric only)

## Distribution
Side: both (clientRequired = true, serverRequired = true)

## Release Process
Follow the standard wendall911 release process in
`../docs/minecraft/MINECRAFT_DEVELOPMENT_NOTES.md`.
