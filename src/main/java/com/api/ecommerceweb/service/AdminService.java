package com.api.ecommerceweb.service;

import com.api.ecommerceweb.dto.CategoryDTO;
import com.api.ecommerceweb.mapper.CategoryMapper;
import com.api.ecommerceweb.model.Category;
import com.api.ecommerceweb.model.File;
import com.api.ecommerceweb.repository.CategoryRepository;
import com.api.ecommerceweb.repository.FileRepository;
import com.api.ecommerceweb.request.CategoryRequest;
import com.api.ecommerceweb.utils.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Component("AdminHelper")
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final CategoryRepository categoryRepo;
    private final FileStorageUtil fileStorageUtil;
    private final FileRepository fileRepo;

    public ResponseEntity<?> getCategories() {
        List<Category> bigParents = categoryRepo.findAllByParentOrderByPosAscNameAsc(null);
        //root
        List<CategoryDTO> categoryDTOS = bigParents.stream().map(CategoryMapper::toCategoryDTO).collect(Collectors.toList());
        //
        for (int i = 0; i < categoryDTOS.size(); i++) {
            getChildren(bigParents.get(i), categoryDTOS.get(i).getChildren(), 1);
        }
        return ResponseEntity.ok(categoryDTOS);
    }

    public void getChildren(Category root, Set<CategoryDTO> rs, int level) {
        List<Category> findByParent = categoryRepo.findAllByParentOrderByPosAscNameAsc(root);
        for (Category c : findByParent) {
            //print level tree
            for (int i = 0; i < level; i++)
                System.out.print("\t");
            log.info(level + "-" + c.getName());
            //
            CategoryDTO parent = CategoryMapper.toCategoryDTO(c);
            rs.add(parent);
//            rs.add(CategoryMapper.toCategoryDTO(c));
            List<Category> allByParent = categoryRepo.findAllByParentOrderByPosAscNameAsc(c);
            if (!allByParent.isEmpty()) {
                getChildren(c, parent.getChildren(), level + 1);
//                getChildren(c, CategoryMapper.toCategoryDTO(c).getChildren(), level + 1);
            }
        }
    }

    public ResponseEntity<?> update(CategoryRequest categoryRequest, MultipartFile file) {
        Category category;
        if (categoryRequest.getId() != null && categoryRepo.existsById(categoryRequest.getId())) {
            category = categoryRepo.getById(categoryRequest.getId());
        } else {
            category = new Category();
        }
        category.setName(categoryRequest.getName());
        category.setPos(categoryRequest.getPos());
        if (categoryRequest.getParentId() != null
                && categoryRepo.existsById(categoryRequest.getParentId())) {
            category.setParent(categoryRepo.getById(categoryRequest.getParentId()));
        }
        if (file != null && !file.isEmpty() && fileStorageUtil.isImage(file)) {
            File f = new File();
            String fileName = fileStorageUtil.storeFile(file, "categories");
            f.setName(fileName);
            f.setSize(file.getSize());
            f.setType(file.getContentType());
            f.setCategory(category);
            category.setImage(f);
            fileRepo.save(f);
        }
        categoryRepo.save(category);
        return ResponseEntity.ok("Update category success");
    }

    public ResponseEntity<?> removeCategory(Long id) {
        Optional<Category> optionalCategory = categoryRepo.findById(id);
        if (optionalCategory.isEmpty())
            return ResponseEntity.notFound().build();
        Category category = optionalCategory.get();
        List<Category> children = categoryRepo.findAllByParentOrderByPosAscNameAsc(category);
        if (category.getParent() != null) {
            children = children.stream().peek(c -> c.setParent(category.getParent())).collect(Collectors.toList());
        } else {
            //get level 1 children
            children = children.stream().peek(c -> c.setParent(null)).collect(Collectors.toList());
        }
        categoryRepo.saveAll(children);
        categoryRepo.delete(category);

        return ResponseEntity.ok("Remove category " + id + " success");
    }
}
