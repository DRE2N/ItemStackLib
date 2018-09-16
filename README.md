## Usage
ItemStackLib provides methods to do two things that the Bukkit API still lacks:
The possibility to apply attributes like attack damage or armor to ItemStacks and
Base64 encoded texture values to player head ItemStacks.
Values to use for heads can be found for example [here](https://freshcoal.com/maincollection) included in commands.

### [JavaDoc](http://erethon.de/javadocs/itemstacklib/)

### Code example
```
// Item attributes
ItemStack sword = new ItemStack(Material.IRON_SWORD);
sword = AttributeWrapper.builder()
        .attribute(InternalAttribute.GENERIC_ATTACK_DAMAGE)
        .name("myattribute")// Optional
        .operation(InternalOperation.ADD_NUMBER)
        .amount(10.35)
        .slots(InternalSlot.HAND)// Optional
        .build()
        .applyTo(sword);

Collection<AttributeWrapper> modifiers = ItemUtil.getAttributes(sword);

// To remove all modifiers of a specific attribute:
ItemUtil.removeAttributeType(sword, InternalAttribute.GENERIC_ATTACK_DAMAGE);
// To remove all modifiers that have a specific name:
ItemUtil.removeAttribute(sword, "myattribute");

// Skulls with Base64 texture value
String texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM2NDVhNDI1OTg3ZjNiN2MyZmFjMDIwNmNlOThiYTVlMjNiOWM0ODUyZmVhNWIxOTc4Zjc0NjdlOGQzMTMifX19";
ItemStack texturedSkull = ItemUtil.setSkullOwner(head, player.getUniqueId(), textureValue);
String textureFromSkull = ItemUtil.getTextureValue(head);
```

## Maven Repository

```
    <dependencies>
        <dependency>
            <groupId>de.erethon</groupId>
            <artifactId>itemstacklib</artifactId>
            <version>2.0</version>
        </dependency>
    </dependencies>
    <repositories>
        <repository>
            <id>dre-repo</id>
            <url>https://erethon.de/repo/</url>
        </repository>
    </repositories>
```

ItemStackLib is a library that developers can shade or copy into their own plugin.
This is not a standalone plugin.
Shading libraries can be done using Maven and its shade plugin:

```
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.1.1</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <artifactSet>
                                <includes>
                                    <include>de.erethon:itemstacklib</include>
                                </includes>
                            </artifactSet>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

## License
Written in 2018 by Daniel Saukel

To the extent possible under law, the author(s) have dedicated all
copyright and related and neighboring rights to this software
to the public domain worldwide.

This software is distributed without any warranty.

You should have received a copy of the CC0 Public Domain Dedication
along with this software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.