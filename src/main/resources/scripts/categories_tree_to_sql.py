import os
import re
from unidecode import unidecode


def to_uniqueName(display_name):
  # Remove accents and convert to lowercase AND Replace ' ' or '_' with '-'
  return re.sub(r'[_\s]+', '-', unidecode(display_name.lower().strip()))


def process_tree_to_sql(tree_string):
  # Split tree in lines and remove blank lines
  lines = [line for line in tree_string.splitlines() if line.strip()]

  # Categories processed (level, uniqueName, displayName)
  categories = []
  # Brands processed (uniqueName, displayName)
  brands = {}
  for i, line in enumerate(lines):
    # Depth level (1 level = 4 spaces)
    level = (len(line) - len(line.lstrip(' '))) // 4
    display_name = line.strip()
    brand_unique_name = None
    if '|' in display_name:
      brand_display_name, display_name = display_name.split('|')
      brand_unique_name = to_uniqueName(brand_display_name)
      brands[brand_unique_name] = brand_display_name
    unique_name = to_uniqueName(display_name.lower())
    categories.append((level, unique_name, display_name, brand_unique_name))

  model_id_counter = 1  # Para generar IDs de modelos
  inserts = []

  # Generate brands inserts
  for brand_unique_name, brand_display_name in brands.items():
    inserts.append(
        f"INSERT INTO BRANDS (UNIQUE_NAME, DISPLAY_NAME) VALUES ('{brand_unique_name}', '{brand_display_name}');")
  inserts.append("")

  # Generate categories and models inserts (categories are the nodes of the tree and models are the leaves)
  for i, category in enumerate(categories):
    level = category[0]
    unique_name = category[1]
    display_name = category[2]
    brand = category[3]

    if level == 0:
      # Root category
      parent_category = None
    else:
      # Category with parent
      parent_category = next(
          category[1] for category in reversed(categories[:i]) if
          category[0] < level)

    if brand is not None:
      inserts.append(f"INSERT INTO MODELS (ID, DISPLAY_NAME, CATEGORY, BRAND) "
                     f"VALUES ({model_id_counter}, '{display_name}', '{parent_category}', '{brand}');")
      model_id_counter += 1
    else:
      inserts.append(
          f"INSERT INTO CATEGORIES (UNIQUE_NAME, DISPLAY_NAME, PARENT_CATEGORY) "
          f"VALUES ('{unique_name}', '{display_name}', {"NULL" if parent_category is None else f"'{parent_category}'"});")

  return "\n".join(inserts)

def import_files_from_directory(directory_path):
  concatenated_tree_string = ""

  # Recorrer todos los archivos en el directorio
  for filename in os.listdir(directory_path):
    file_path = os.path.join(directory_path, filename)

    # Verificar si es un archivo y leer su contenido
    if os.path.isfile(file_path):
      with open(file_path, 'r', encoding='utf-8') as file:
        concatenated_tree_string += file.read() + "\n"

  return concatenated_tree_string

directory_path = './input'
# Copiar este arbol -> https://www.chollo.es/categoria/electronica
# Nodos intermedios -> Uno por linea, los hijos tienen una identacion + 4 espacioes
# Nodos hoja -> tiene como prefijo <marca>|
tree_string = import_files_from_directory(directory_path)

# Llamar a la función para generar los SQL INSERT
sql_inserts = process_tree_to_sql(tree_string)
print(sql_inserts)
