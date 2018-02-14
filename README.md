## License
Written in 2018 by Daniel Saukel

To the extent possible under law, the author(s) have dedicated all
copyright and related and neighboring rights to this software
to the public domain worldwide.

This software is distributed without any warranty.

You should have received a copy of the CC0 Public Domain Dedication
along with this software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.

## Usage
```
// Item attributes
AttributeWrapper attribute = new AttributeWrapper(Attribute.GENERIC_ATTACK_DAMAGE, 6.0, Operation.ADD_NUMBER, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND);
ItemStack itemWithAttribute = attribute.applyTo(itemWithoutAttribute);

// Skulls with Base64 texture value
String texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM2NDVhNDI1OTg3ZjNiN2MyZmFjMDIwNmNlOThiYTVlMjNiOWM0ODUyZmVhNWIxOTc4Zjc0NjdlOGQzMTMifX19";
ItemStack texturedSkull = ItemUtil.setSkullOwner(head, player.getUniqueId(), textureValue);
```

## Maven Repository

```
    <dependencies>
        <dependency>
            <groupId>de.erethon</groupId>
            <artifactId>itemstacklib</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>
    <repositories>
        <repository>
            <id>dre-repo</id>
            <url>https://erethon.de/repo/</url>
        </repository>
    </repositories>
```