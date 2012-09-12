# class definition, no curly braket
class Thing

    @@num_things = 0            # class variable, start with two @, like static member of java
    
    A = 10                      # class constant, naming with upper case character
    class Home                  # static class of Thing, similar with constant
    end

    # I feel confusing here: sometimes, there is space between symbol and variable name, sometimes not, not consistent design.
    # ANSWER: attr_accessor is a method, the following is a method invocation, the :name is an argument of that method, equals to : attr_accessor(:name, :description)
    attr_accessor :name         # there must be no space between : and name
    attr_accessor :description  # the other two ways to define getter/setter: 1).attr_reader & attr_writer, 2).def property & def property=(aProp)

    def initialize(aName, aDescription) # constructor
        @name = aName           # @name is instance variable, like java property
        @description = aDescription
        @@num_things += 1
    end

    def to_s
        return "The #{name} is #{description}"
    end

    def show_class_var
        return "there is #{@@num_things} things in total"       # the class variables can not be access outside 
    end
end

class Animal < Thing            # < is inherit
    attr_reader :gender         # read-only

    def initialize(aName, aDescription, gender)
        super(aName, aDescription)
        @gender = gender
    end

    def to_s
        return super + " and it's #{gender}"    # super is calling methods of super class
    end
end

cat = Animal.new('Miao', 'a cat', 'femal')
puts cat.show_class_var         # for method invocation, the braket for arguments can be omitted

#The following set invocation will fail, since the gender is read-only
#cat.gender = 'male'

# print an object, equals to print(cat.to_s)
puts(cat)

cat2 = Animal.new('Wu', 'a cat', 'femal')
puts(cat2.show_class_var)

# how to access class constant
puts(Thing::A)
puts Thing::Home.new
