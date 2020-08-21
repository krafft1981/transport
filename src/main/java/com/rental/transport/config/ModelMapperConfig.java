
@Configuration
public ModelMapperConfig() {
  @Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
}
