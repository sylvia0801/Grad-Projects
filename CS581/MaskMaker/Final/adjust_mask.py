#Some code from: https://blender.stackexchange.com/questions/43335/how-do-i-get-knife-project-operator-to-use-view-settings-within-the-operator
#Some code from: https://blender.stackexchange.com/questions/41277/automatic-select-vertices-by-given-coordinates

import bpy
import bmesh
import json
from mathutils import Euler, Vector, kdtree

C = bpy.context
D = bpy.data

current_width = 14.5
pixel_width = 0.000555 # Temp Value, take it as an input from JSON as well. 
pixel_size = pixel_width * 100 # Multiplies by 100 because Blender is scaled up

class Coord(object):

    def __init__(self, x, y, z, py = None, px = None, translateX = None, translateY = None, translateZ = 0.0):
        self.x = x
        self.y = y
        self.z = z
        self.py = py # Pixel value of Y in picture
        self.px = px # Picture value of X in picture
        self.translateX = translateX
        self.translateY = translateY
        self.translateZ = translateZ
    
    def getX(self):
        return self.x

    def getY(self):
        return self.y

    def getZ(self):
        return self.z

    def getPY(self):
        return self.py

    def getPX(self):
        return self.px

    def getTranslateX(self):
        return self.translateX

    def getTranslateY(self):
        return self.translateY

    def getTranslateZ(self):
        return self.translateZ

    def setX(self, x):
        self.x = x

    def setY(self, y):
        self.y = y

    def setZ(self, z):
        self.z = z

    def setP(self, py, px):
        self.py = py
        self.px = px 

    def setTranslateX(self, tx):
        self.translateX = tx

    def setTranslateY(self, ty):
        self.translateY = ty

    def getTuple(self):
        return (self.x, self.y, self.z)

    def getTranslationTuple(self):
        return (self.translateX, self.translateY, self.translateZ)

def view3d_find( return_area = False ):
    
    # returns first 3d view, normally we get from context
    for area in bpy.context.window.screen.areas:
        if area.type == 'VIEW_3D':
            v3d = area.spaces[0]
            rv3d = v3d.region_3d
            for region in area.regions:
                if region.type == 'WINDOW':
                    if return_area: return region, rv3d, v3d, area
                    return region, rv3d, v3d
    return None, None

def add_eyeholes(eye1x, eye1y, eye2x, eye2y, eyeWidth, eyeHeight):
    
    region, rv3d, v3d, area = view3d_find(True)
    # Set view to TOP by directly rotating the 3D view region's view_rotation
    rv3d.view_rotation = Euler( (0,0,0) ).to_quaternion()

    C.scene.cursor_location = (0.0, 0.0, 0.0)

    bpy.ops.mesh.primitive_circle_add(location = (eye1x, eye1y, 5))
    C.object.name = 'Eye1'

    C.object.dimensions = eyeWidth, eyeHeight, 0.0

    D.objects['Mask'].select = True


    # Define context override dictionary for overriding the knife_project operator's context
    override = {
        'scene'            : bpy.context.scene,
        'region'           : region,
        'area'             : area,
        'space'            : v3d,
        'active_object'    : D.objects['Mask'],
        'window'           : bpy.context.window,
        'screen'           : bpy.context.screen,
        'selected_objects' : bpy.context.selected_objects,
        'edit_object'      : D.objects['Mask']
    }

    C.scene.objects.active = D.objects['Mask']
    bpy.ops.object.mode_set(mode = 'EDIT')

    # Force redraw the scene - this is considered unsavory but is necessary here
    bpy.ops.wm.redraw_timer(type='DRAW_WIN_SWAP', iterations=1)

    bpy.ops.mesh.knife_project(override)
    bpy.ops.mesh.delete(type = 'FACE')
    bpy.ops.object.mode_set(mode = 'OBJECT')

    D.objects['Eye1'].select = True
    D.objects['Mask'].select = False
    bpy.ops.object.delete()

    bpy.ops.mesh.primitive_circle_add(location = (eye2x, eye2y, 5))
    C.object.name = 'Eye2'

    C.object.dimensions = eyeWidth, eyeHeight, 0.0

    D.objects['Mask'].select = True


    # Define context override dictionary for overriding the knife_project operator's context
    override = {
        'scene'            : bpy.context.scene,
        'region'           : region,
        'area'             : area,
        'space'            : v3d,
        'active_object'    : D.objects['Mask'],
        'window'           : bpy.context.window,
        'screen'           : bpy.context.screen,
        'selected_objects' : bpy.context.selected_objects,
        'edit_object'      : D.objects['Mask']
    }

    C.scene.objects.active = D.objects['Mask']
    bpy.ops.object.mode_set(mode = 'EDIT')

    # Force redraw the scene - this is considered unsavory but is necessary here
    bpy.ops.wm.redraw_timer(type='DRAW_WIN_SWAP', iterations=1)

    bpy.ops.mesh.knife_project(override)
    bpy.ops.mesh.delete(type = 'FACE')
    bpy.ops.object.mode_set(mode = 'OBJECT')

    D.objects['Eye2'].select = True
    D.objects['Mask'].select = False
    bpy.ops.object.delete()

def movePoints(oldPoints, howMuchToTranslateBy):
    bpy.ops.object.mode_set(mode = 'EDIT')
    obj = C.edit_object
    bm = bmesh.from_edit_mesh(obj.data)

    tree = kdtree.KDTree(len(bm.verts))

    for i, vertex in enumerate(bm.verts):
        vertex.select = True
        tree.insert(vertex.co, i)
    tree.balance()

    bm.verts.ensure_lookup_table()

    pointToEdit = None
    for i, vertex in enumerate(oldPoints):
        pointCoord, index, dist = tree.find(vertex)
        if i == 0:
            pointToEdit = bm.verts[index]
        bm.verts[index].select = False

    bpy.ops.mesh.hide(unselected = False)
    pointToEdit.select = True
    bpy.ops.transform.translate(value = howMuchToTranslateBy, proportional = 'ENABLED', proportional_size = 11)
    pointToEdit.select = False   
    bpy.ops.mesh.reveal()

    bpy.ops.object.mode_set(mode = 'OBJECT')


def calcTranslateX(self, other, pxl_size, width_mult):
    transX = (self.getPX() - other.getPX()) * pxl_size * width_mult
    return (transX - (self.getX() - other.getX()))

def calcTranslateY(self, other, pxl_size, height_mult):
    transY = (other.getPY() - self.getPY()) * pxl_size * height_mult
    return (transY - (self.getY() - other.getY()))

with open('points.json') as json_file:

    # When reading the JSON file in Blender, make sure that you are grabbing the JSON file from the correct location, as it isn't located
    # in the same area as a Python script in Blender.
    data = json.load(json_file)

    # Pixel Width
    pixel_size = data['pixel_width']

    # Nose Lower Middle
    nose_lower_middle = Coord(0.0,-4.6,3.18, data['nose_contour_lower_middle']['y'], data['nose_contour_lower_middle']['x'])
    nose_lower_middle_left1 = Coord(-0.82,-4.44,3.18)
    nose_lower_middle_left2 = Coord(-1.54,-4.23,3.18)
    nose_lower_middle_right1 = Coord(0.82,-4.44,3.18)
    nose_lower_middle_right2 = Coord(1.54,-4.23,3.18)

    # Nose Tip
    nose_tip = Coord(0.0,-3.37,4.21, data['nose_tip']['y'], data['nose_tip']['x'])
    nose_tip_1 = Coord(0.0,-2.75,4.34)
    nose_tip_2 = Coord(0.0,-2.13,4.46)
    nose_tip_3 = Coord(0.0,-1.34,4.23)

    # Nose Left
    nose_left = Coord(-2.16,-3.7,3.18, data['nose_left']['y'], data['nose_left']['x'])
    nose_left_left_1 = Coord(-2.05,-3.09,3.3)
    nose_left_left_2 = Coord(-1.67,-2.49,3.3)
    nose_left_left_3 = Coord(-1.56,-1.88,3.06)
    nose_left_right_1 = Coord(-1.0,-3.33,4.21)
    nose_left_right_2 = Coord(-1.0,-2.77,4.38)
    nose_left_right_3 = Coord(-1.0,-2.08,4.38)
    nose_left_right_4 = Coord(-0.8,-1.33,4.17)

    # Nose Right
    nose_right = Coord(2.16,-3.7,3.18, data['nose_right']['y'], data['nose_right']['x'])
    nose_right_right_1 = Coord(2.05,-3.09,3.3)
    nose_right_right_2 = Coord(1.67,-2.49,3.3)
    nose_right_right_3 = Coord(1.56,-1.88,3.06)
    nose_right_left_1 = Coord(1.0,-3.33,4.21)
    nose_right_left_2 = Coord(1.0,-2.77,4.38)
    nose_right_left_3 = Coord(1.0,-2.08,4.38)
    nose_right_left_4 = Coord(0.8,-1.33,4.17)

    # Contour Chin
    contour_chin_1 = Coord(0.0,-12.3,0.0, data['contour_chin']['y'], data['contour_chin']['x']) # We will use this as the anchor point.
    contour_chin_2 = Coord(0.0,-10.0,1.61)
    contour_chin_3 = Coord(0.0,-8.0,2.35)
    contour_chin_4 = Coord(0.0,-6.07,2.85)

    # Face Height Parameters
    face_height = (contour_chin_1.getPY() - nose_lower_middle.getPY()) * pixel_size * 3.0
    height_mult = 22.3/face_height
    height_ratio = face_height/22.3

    # Contour Top (Top of Face Mask)
    contour_top_2 = Coord(0.0,10.0,0.0)
    contour_top_2 = Coord(0.0,7.0,1.61)
    contour_top_3 = Coord(0.0,5.46,2.35)
    contour_top_4 = Coord(0.0,4.11,2.85)

    # Contour Left 9
    contour_left9_1 = Coord(-1.54,-12.0,0.0, data['contour_left9']['y'], data['contour_left9']['x'])
    contour_left9_2 = Coord(-1.37,-9.81,1.61)
    contour_left9_3 = Coord(-1.22,-7.84,2.35)
    contour_left9_4 = Coord(-1.02,-5.93,2.85)

    # Contour Left 8
    contour_left8_1 = Coord(-3.0,-11.21,0.0, data['contour_left8']['y'], data['contour_left8']['x'])
    contour_left8_2 = Coord(-2.61,-9.26,1.61)
    contour_left8_3 = Coord(-2.39,-7.33,2.35)
    contour_left8_4 = Coord(-2.0,-5.52,2.85)

    # Contour Left 7
    contour_left7_1 = Coord(-4.32,-10.04,0.0, data['contour_left7']['y'], data['contour_left7']['x'])
    contour_left7_2 = Coord(-3.59,-8.37,1.61)
    contour_left7_3 = Coord(-4.32,-10.04,2.35)
    contour_left7_4 = Coord(-2.67,-4.85,2.85)

    # Contour Left 6
    contour_left6_1 = Coord(-5.5,-8.25,0.0, data['contour_left6']['y'], data['contour_left6']['x'])
    contour_left6_2 = Coord(-4.48,-7.0,1.61)
    contour_left6_3 = Coord(-3.73,-5.45,2.35)
    contour_left6_4 = Coord(-3.09,-4.03,2.85)

    # Contour Left 5
    contour_left5_1 = Coord(-6.21,-6.46,0.0, data['contour_left5']['y'], data['contour_left5']['x'])
    contour_left5_2 = Coord(-5.04,-5.64,1.61)
    contour_left5_3 = Coord(-4.13,-4.38,2.35)
    contour_left5_4 = Coord(-3.33,-3.24,2.85)

    # Contour Left 4
    contour_left4_1 = Coord(-6.75,-4.5,0.0, data['contour_left4']['y'], data['contour_left4']['x'])
    contour_left4_2 = Coord(-5.52,-3.93,1.61)
    contour_left4_3 = Coord(-4.48,-3.07,2.35)
    contour_left4_4 = Coord(-3.53,-2.28,2.85)

    # Contour Left 3
    contour_left3_1 = Coord(-7.11,-2.32,0.0, data['contour_left3']['y'], data['contour_left3']['x'])
    contour_left3_2 = Coord(-5.84,-1.97,1.61)
    contour_left3_3 = Coord(-4.78,-1.57,2.35)
    contour_left3_4 = Coord(-3.69,-1.18,2.85)

    # Contour Left 2
    contour_left2_1 = Coord(-7.25,0.0,0.0, data['contour_left2']['y'], data['contour_left2']['x'])
    contour_left2_2 = Coord(-5.94,0.0,1.61)
    contour_left2_3 = Coord(-4.86,0.0,2.35)
    contour_left2_4 = Coord(-3.75,0.0,2.85)

    # Contour Left 1
    contour_left1_1 = Coord(-7.25,1.96,0.0, data['contour_left1']['y'], data['contour_left1']['x'])
    contour_left1_2 = Coord(-5.92,1.43,1.61)
    contour_left1_3 = Coord(-4.84,1.12,2.35)
    contour_left1_4 = Coord(-3.77,0.9,2.85)

    # Contour Right 9
    contour_right9_1 = Coord(1.54,-12.0,0.0, data['contour_right9']['y'], data['contour_right9']['x'])
    contour_right9_2 = Coord(1.37,-9.81,1.61)
    contour_right9_3 = Coord(1.22,-7.84,2.35)
    contour_right9_4 = Coord(1.02,-5.93,2.85)

    # Contour Right 8
    contour_right8_1 = Coord(3.0,-11.21,0.0, data['contour_right8']['y'], data['contour_right8']['x'])
    contour_right8_2 = Coord(2.61,-9.26,1.61)
    contour_right8_3 = Coord(2.39,-7.33,2.35)
    contour_right8_4 = Coord(2.0,-5.52,2.85)

    # Contour Right 7
    contour_right7_1 = Coord(4.32,-10.04,0.0, data['contour_right7']['y'], data['contour_right7']['x'])
    contour_right7_2 = Coord(3.59,-8.37,1.61)
    contour_right7_3 = Coord(4.32,-10.04,2.35)
    contour_right7_4 = Coord(2.67,-4.85,2.85)

    # Contour Right 6
    contour_right6_1 = Coord(5.5,-8.25,0.0, data['contour_right6']['y'], data['contour_right6']['x'])
    contour_right6_2 = Coord(4.48,-7.0,1.61)
    contour_right6_3 = Coord(3.73,-5.45,2.35)
    contour_right6_4 = Coord(3.09,-4.03,2.85)

    # Contour Right 5
    contour_right5_1 = Coord(6.21,-6.46,0.0, data['contour_right5']['y'], data['contour_right5']['x'])
    contour_right5_2 = Coord(5.04,-5.64,1.61)
    contour_right5_3 = Coord(4.13,-4.38,2.35)
    contour_right5_4 = Coord(3.33,-3.24,2.85)

    # Contour Right 4
    contour_right4_1 = Coord(6.75,-4.5,0.0, data['contour_right4']['y'], data['contour_right4']['x'])
    contour_right4_2 = Coord(5.52,-3.93,1.61)
    contour_right4_3 = Coord(4.48,-3.07,2.35)
    contour_right4_4 = Coord(3.53,-2.28,2.85)

    # Contour Right 3
    contour_right3_1 = Coord(7.11,-2.32,0.0, data['contour_right3']['y'], data['contour_right3']['x'])
    contour_right3_2 = Coord(5.84,-1.97,1.61)
    contour_right3_3 = Coord(4.78,-1.57,2.35)
    contour_right3_4 = Coord(3.69,-1.18,2.85)

    # Contour Right 2
    contour_right2_1 = Coord(7.25,0.0,0.0, data['contour_right2']['y'], data['contour_right2']['x'])
    contour_right2_2 = Coord(5.94,0.0,1.61)
    contour_right2_3 = Coord(4.86,0.0,2.35)
    contour_right2_4 = Coord(3.75,0.0,2.85)

    # Contour Right 1
    contour_right1_1 = Coord(7.25,1.96,0.0, data['contour_right1']['y'], data['contour_right1']['x'])
    contour_right1_2 = Coord(5.92,1.43,1.61)
    contour_right1_3 = Coord(4.84,1.12,2.35)
    contour_right1_4 = Coord(3.77,0.9,2.85)

    # Face Width Parameters
    face_width = (contour_right2_1.getPX() - contour_left2_1.getPX()) * pixel_size
    width_mult = 14.5/face_width
    width_ratio = face_width/14.5

    # Relevant nose contour points to move. These also have attached relevant points for proportional editing in the list of lists.

    nose_tip.setTranslateX(calcTranslateX(nose_tip, contour_chin_1, pixel_size, width_mult))
    nose_lower_middle.setTranslateX(calcTranslateX(nose_lower_middle, contour_chin_1, pixel_size, width_mult))
    nose_left.setTranslateX(calcTranslateX(nose_left, contour_chin_1, pixel_size, width_mult))
    nose_right.setTranslateX(calcTranslateX(nose_right, contour_chin_1, pixel_size, width_mult))

    nose_tip.setTranslateY(calcTranslateY(nose_tip, contour_chin_1, pixel_size, height_mult))
    nose_lower_middle.setTranslateY(calcTranslateY(nose_lower_middle, contour_chin_1, pixel_size, height_mult))
    nose_left.setTranslateY(calcTranslateY(nose_left, contour_chin_1, pixel_size, height_mult))
    nose_right.setTranslateY(calcTranslateY(nose_right, contour_chin_1, pixel_size, height_mult))

    # This just finds out the translation numbers for all of the contours. Z axis translation should be 0.0.
    # Only relevant contours are the ones that are found by the API. The rest are scaled. All of these have a multiplier attached so
    # once scaling is done to the entire object, these points will not be affected by the scaling so the ratios of where they are
    # relative to the anchor point should remain what the API predicts it will be. 

    contour_left9_1.setTranslateX(calcTranslateX(contour_left9_1, contour_chin_1, pixel_size, width_mult))
    contour_left8_1.setTranslateX(calcTranslateX(contour_left8_1, contour_chin_1, pixel_size, width_mult))
    contour_left7_1.setTranslateX(calcTranslateX(contour_left7_1, contour_chin_1, pixel_size, width_mult))
    contour_left6_1.setTranslateX(calcTranslateX(contour_left6_1, contour_chin_1, pixel_size, width_mult))
    contour_left5_1.setTranslateX(calcTranslateX(contour_left5_1, contour_chin_1, pixel_size, width_mult))
    contour_left4_1.setTranslateX(calcTranslateX(contour_left4_1, contour_chin_1, pixel_size, width_mult))
    contour_left3_1.setTranslateX(calcTranslateX(contour_left3_1, contour_chin_1, pixel_size, width_mult))
    contour_left2_1.setTranslateX(calcTranslateX(contour_left2_1, contour_chin_1, pixel_size, width_mult))
    contour_left1_1.setTranslateX(calcTranslateX(contour_left1_1, contour_chin_1, pixel_size, width_mult))
    
    contour_left9_1.setTranslateY(calcTranslateY(contour_left9_1, contour_chin_1, pixel_size, height_mult))
    contour_left8_1.setTranslateY(calcTranslateY(contour_left8_1, contour_chin_1, pixel_size, height_mult))
    contour_left7_1.setTranslateY(calcTranslateY(contour_left7_1, contour_chin_1, pixel_size, height_mult))
    contour_left6_1.setTranslateY(calcTranslateY(contour_left6_1, contour_chin_1, pixel_size, height_mult))
    contour_left5_1.setTranslateY(calcTranslateY(contour_left5_1, contour_chin_1, pixel_size, height_mult))
    contour_left4_1.setTranslateY(calcTranslateY(contour_left4_1, contour_chin_1, pixel_size, height_mult))
    contour_left3_1.setTranslateY(calcTranslateY(contour_left3_1, contour_chin_1, pixel_size, height_mult))
    contour_left2_1.setTranslateY(calcTranslateY(contour_left2_1, contour_chin_1, pixel_size, height_mult))
    contour_left1_1.setTranslateY(calcTranslateY(contour_left1_1, contour_chin_1, pixel_size, height_mult))

    contour_right9_1.setTranslateX(calcTranslateX(contour_right9_1, contour_chin_1, pixel_size, width_mult))
    contour_right8_1.setTranslateX(calcTranslateX(contour_right8_1, contour_chin_1, pixel_size, width_mult))
    contour_right7_1.setTranslateX(calcTranslateX(contour_right7_1, contour_chin_1, pixel_size, width_mult))
    contour_right6_1.setTranslateX(calcTranslateX(contour_right6_1, contour_chin_1, pixel_size, width_mult))
    contour_right5_1.setTranslateX(calcTranslateX(contour_right5_1, contour_chin_1, pixel_size, width_mult))
    contour_right4_1.setTranslateX(calcTranslateX(contour_right4_1, contour_chin_1, pixel_size, width_mult))
    contour_right3_1.setTranslateX(calcTranslateX(contour_right3_1, contour_chin_1, pixel_size, width_mult))
    contour_right2_1.setTranslateX(calcTranslateX(contour_right2_1, contour_chin_1, pixel_size, width_mult))
    contour_right1_1.setTranslateX(calcTranslateX(contour_right1_1, contour_chin_1, pixel_size, width_mult))
    
    contour_right9_1.setTranslateY(calcTranslateY(contour_right9_1, contour_chin_1, pixel_size, height_mult))
    contour_right8_1.setTranslateY(calcTranslateY(contour_right8_1, contour_chin_1, pixel_size, height_mult))
    contour_right7_1.setTranslateY(calcTranslateY(contour_right7_1, contour_chin_1, pixel_size, height_mult))
    contour_right6_1.setTranslateY(calcTranslateY(contour_right6_1, contour_chin_1, pixel_size, height_mult))
    contour_right5_1.setTranslateY(calcTranslateY(contour_right5_1, contour_chin_1, pixel_size, height_mult))
    contour_right4_1.setTranslateY(calcTranslateY(contour_right4_1, contour_chin_1, pixel_size, height_mult))
    contour_right3_1.setTranslateY(calcTranslateY(contour_right3_1, contour_chin_1, pixel_size, height_mult))
    contour_right2_1.setTranslateY(calcTranslateY(contour_right2_1, contour_chin_1, pixel_size, height_mult))
    contour_right1_1.setTranslateY(calcTranslateY(contour_right1_1, contour_chin_1, pixel_size, height_mult))
    
    list_of_coords = [
                    [contour_left9_1, contour_left9_2, contour_left9_3, contour_left9_4],
                    [contour_left8_1, contour_left8_2, contour_left8_3, contour_left8_4],
                    [contour_left7_1, contour_left7_2, contour_left7_3, contour_left7_4],
                    [contour_left6_1, contour_left6_2, contour_left6_3, contour_left6_4],
                    [contour_left5_1, contour_left5_2, contour_left5_3, contour_left5_4],
                    [contour_left4_1, contour_left4_2, contour_left4_3, contour_left4_4],
                    [contour_left3_1, contour_left3_2, contour_left3_3, contour_left3_4],
                    [contour_left2_1, contour_left2_2, contour_left2_3, contour_left2_4],
                    [contour_left1_1, contour_left1_2, contour_left1_3, contour_left1_4],
                    [contour_right9_1, contour_right9_2, contour_right9_3, contour_right9_4],
                    [contour_right8_1, contour_right8_2, contour_right8_3, contour_right8_4],
                    [contour_right7_1, contour_right7_2, contour_right7_3, contour_right7_4],
                    [contour_right6_1, contour_right6_2, contour_right6_3, contour_right6_4],
                    [contour_right5_1, contour_right5_2, contour_right5_3, contour_right5_4],
                    [contour_right4_1, contour_right4_2, contour_right4_3, contour_right4_4],
                    [contour_right3_1, contour_right3_2, contour_right3_3, contour_right3_4],
                    [contour_right2_1, contour_right2_2, contour_right2_3, contour_right2_4],
                    [contour_right1_1, contour_right1_2, contour_right1_3, contour_right1_4],
                    [nose_lower_middle, nose_lower_middle_left1, nose_lower_middle_left2, nose_lower_middle_right1, nose_lower_middle_right2],
                    [nose_tip, nose_tip_1, nose_tip_2, nose_tip_3],
                    [nose_left, nose_left_left_1, nose_left_left_2, nose_left_left_3, nose_left_right_1, nose_left_right_2, nose_left_right_3, nose_left_right_4],
                    [nose_right, nose_right_right_1, nose_right_right_2, nose_right_right_3, nose_right_left_1, nose_right_left_2, nose_right_left_3, nose_right_left_4]
                    ]

                

    # Right Eye Center Co-od. Not in Coord object, get the X, Y location in Blender post mask shrink.
    right_eye_centerY = (contour_chin_1.getY() + ((contour_chin_1.getPY() - data['right_eye_center']['y']) * pixel_size * height_mult))
    right_eye_centerX = (contour_chin_1.getX() + ((data['right_eye_center']['x'] - contour_chin_1.getPX()) * pixel_size * width_mult))

    # Left Eye Center Co-od.
    left_eye_centerY = (contour_chin_1.getY() + ((contour_chin_1.getPY() - data['left_eye_center']['y']) * pixel_size * height_mult))
    left_eye_centerX = (contour_chin_1.getX() + ((data['left_eye_center']['x'] - contour_chin_1.getPX()) * pixel_size * width_mult))

    # Calculating Eye dimensions.
    left_sizeX = ((data['left_eye_right_corner']['x'] - data['left_eye_left_corner']['x']) * pixel_size) * width_mult
    left_sizeY = ((data['left_eye_bottom']['y'] - data['left_eye_top']['y']) * pixel_size) * height_mult

    right_sizeX = ((data['right_eye_right_corner']['x'] - data['right_eye_left_corner']['x']) * pixel_size) * width_mult
    right_sizeY = ((data['right_eye_bottom']['y'] - data['right_eye_top']['y']) * pixel_size) * height_mult

    # Final eye dimensions. Use these when creating eye holes.
    eye_width = max(left_sizeX, right_sizeX) # Perhaps add 0.5 to the width for leniency
    eye_height = max(left_sizeY, right_sizeY) # Perhaps add 1.0 to the height for leniency
        
D.objects['Mask'].dimensions = [face_width / 2, face_height / 2, D.objects['Mask'].dimensions[2]]
C.scene.objects.active = D.objects['Mask']
C.scene.tool_settings.use_proportional_edit_objects = True
C.scene.tool_settings.proportional_size = 11

for c in list_of_coords:
    points = []
    for element in c:
        points.append(element.getTuple())
    howMuchToTranslateBy = c[0].getTranslationTuple()
    movePoints(points, howMuchToTranslateBy)


add_eyeholes(left_eye_centerX, left_eye_centerY, right_eye_centerX, right_eye_centerY, eye_width, eye_height)
bpy.ops.object.modifier_add(type='SOLIDIFY')
C.object.modifiers["Solidify"].thickness = 0.4
C.object.modifiers.new("subd", type='SUBSURF')
C.object.modifiers['subd'].levels = 2
