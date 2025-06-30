# Hexdump

Hexdump is an addon for Hex Casting which adds a command, `/hexdump`, which writes all the patterns from every installed addon into a `patterns.json` file in your `.minecraft` folder.

This is, perhaps, a little bit overpowered, seeing as how I do not exclude Great Spells from this listing.

## TODO:
- [ ] Make the command operator-only.
- [ ] Make a separate command or something to exclude per-world patterns.
  - Maybe the version *without* per-world patterns could then be a non-op command?
- [ ] Test how this works on servers.
  - I'm like 80% sure that this currently writes to the *server's* .minecraft folder instead of the client's, and I just don't care enough to fix it right now.
    - [ ] Probably should fix that at some point.
- [ ] Dump the translations for the patterns to add a name field to `patterns.json`.
