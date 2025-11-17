# Este script se encarga de configurar el archivo M32.circ en el perfil "release".
import xml.etree.ElementTree as ET

debug_name = "jar#plugins/dist/M32Library.jar#m32plugin.M32Library"
release_name = "jar#lib/M32Library.jar#m32plugin.M32Library"

tree = ET.parse("M32.circ")
root = tree.getroot()

for child in root:
    if child.tag == "lib" and child.attrib['desc'] == debug_name:
        child.attrib['desc'] = release_name

tree.write("M32 Release.circ")
