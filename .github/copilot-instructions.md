# LiteWorldEdit — Project Guidelines

## Overview

A lightweight WorldEdit alternative for Minecraft survival servers (Paper/Folia 1.20.1+). Executes block operations asynchronously via a per-player job queue to prevent server lag. Written in Java 17 with Maven.

## Build

```bash
mvn clean package          # Build shaded JAR → target/LiteWorldEdit-*.jar
```

Dependencies: Folia API (provided), Adventure Platform Bukkit (shaded).

## Architecture

```
LiteWorldEdit.java          → Plugin entry point (onEnable/onDisable)
Commands.java               → All subcommands as static SecondaryCommand fields
Configuration.java          → Annotation-driven YAML config (extends ConfigurationFile)
Events.java                 → Player join/quit, block selection via interact events
Task.java                   → Per-tick job executor (Runnable polled by scheduler)

JobGenerator/               → Breaks cuboid selection into atomic Job objects
  Fill, Drain, Empty, OverLay

Jobs/                       → Atomic block operations with error codes
  Job (abstract), Place, Remove, Absorb, JobErrCode

Managers/                   → Player state and job lifecycle
  Cache (singleton), XPlayer (per-player), JobQueue, Point

utils/                      → Reusable framework code
  command/                  → Declarative command framework (CommandManager, SecondaryCommand)
  configuration/            → Reflection-based YAML config with annotations
  scheduler/                → Folia/Spigot scheduler abstraction (CancellableTask)
```

**Job Pipeline**: Command → JobGenerator creates Jobs → XPlayer.JobQueue stores them → Task pops N jobs/tick (N = `Configuration.multiplier`) → Job.execution() returns JobErrCode (OK / skip ≥200 / pause <200).

## Conventions

- **Language**: All user-facing strings (notifications, logs, config comments) are in **Chinese (zh-CN)**.
- **Commands**: Defined as `static SecondaryCommand` fields in `Commands.java` using anonymous inner class + fluent builder pattern. Call `.register()` at the end.
- **Configuration**: Annotate fields in `Configuration.java`. Nested config uses inner classes extending `ConfigurationPart`. Annotations: `@Comments`, `@PreProcess`, `@PostProcess`, `@HandleManually`.
- **Naming**: Classes PascalCase, fields camelCase, private fields prefixed `_` (e.g., `_block`, `_debug`). YAML keys auto-converted to kebab-case.
- **Permissions**: Hierarchical dot notation under `lwe.*` (e.g., `lwe.command.fill`).
- **Scheduler**: Always use `Scheduler.*` methods (never raw Bukkit scheduler) for Folia compatibility. Use `runAtFixedRateEntity()` for player-bound tasks.
- **Logging**: Use `XLogger.info/warn/error/debug()` with `{0}`, `{1}` format placeholders.
- **Job types**: New operations need a `Job` subclass (in `Jobs/`) and a generator (in `JobGenerator/`). Jobs must return `JobErrCode` from `execution()`.
- **Events**: Fired at `EventPriority.LOWEST` for selection. `BlockBreakEvent`/`BlockPlaceEvent` are simulated inside jobs for permission plugin compatibility.

## Key Design Decisions

- **Async queue, not instant**: All operations queue into per-player `JobQueue` and execute one-at-a-time per tick to prevent TPS drops.
- **Survival-oriented**: Place consumes inventory items (auto-pulls from Shulker Boxes). Remove requires Netherite Pickaxe with durability cost.
- **Distance limit**: 128 blocks max from player (hardcoded in `Job.notInRange()`).
- **BossBar progress**: Each `JobQueue` shows a BossBar with completion % and ETA.
