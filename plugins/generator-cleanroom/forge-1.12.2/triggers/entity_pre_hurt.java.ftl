@SubscribeEvent public void onEntityPreHurt(LivingDamageEvent event){
		if(event!=null&&event.getSource()!=null){
		Entity sourceEntity=event.getSource().getTrueSource();
		Entity entity=event.getEntityLiving();
		int amount=(int)event.getAmount();
		int i=(int)entity.posX;
		int j=(int)entity.posY;
		int k=(int)entity.posZ;
		World world=entity.world;
		java.util.HashMap<String, Object> dependencies=new java.util.HashMap<>();
		dependencies.put("x",i);
		dependencies.put("y",j);
		dependencies.put("z",k);
		dependencies.put("world",world);
		dependencies.put("entity",entity);
		dependencies.put("sourceentity", sourceEntity);
		dependencies.put("amount", amount);
		dependencies.put("event",event);
		this.executeProcedure(dependencies);
		}
		}